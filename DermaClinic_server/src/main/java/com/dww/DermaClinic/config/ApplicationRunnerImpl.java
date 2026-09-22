package com.dww.DermaClinic.config;

import com.dww.DermaClinic.entity.Permission;
import com.dww.DermaClinic.entity.Role;
import com.dww.DermaClinic.entity.User;
import com.dww.DermaClinic.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationRunnerImpl implements ApplicationRunner {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.count() == 0) {
            log.info("No user found in database. Initializing default roles and admin account...");

            // 1. Create permissions
            Permission readUser = Permission.builder()
                    .name("USER_READ")
                    .description("Permission to view user profile")
                    .build();

            Permission writeUser = Permission.builder()
                    .name("USER_WRITE")
                    .description("Permission to create/edit user profile")
                    .build();

            Set<Permission> adminPermissions = new HashSet<>();
            adminPermissions.add(readUser);
            adminPermissions.add(writeUser);

            // 2. Create roles (Admin role has permissions, User role is standard)
            Role adminRole = Role.builder()
                    .name("ADMIN")
                    .description("Administrator role")
                    .permissions(adminPermissions)
                    .build();

            Role userRole = Role.builder()
                    .name("USER")
                    .description("Standard user role")
                    .build();

            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);
            adminRoles.add(userRole);

            // 3. Create Admin user with both roles attached (persisted via cascade)
            User admin = User.builder()
                    .firstName("Admin")
                    .lastName("Admin")
                    .email("admin@gmail.com")
                    .phoneNumber("0987654321")
                    .password(passwordEncoder.encode("Asdf1234!"))
                    .enabled(true)
                    .emailVerified(true)
                    .roles(adminRoles)
                    .build();

            userRepository.save(admin);
            log.info("Default admin user created successfully (phone: {}, password: Asdf1234!). Roles & permissions saved via cascade.", admin.getPhoneNumber());
        } else {
            log.info("Users already exist in database (count: {}). Skipping initialization.", userRepository.count());
        }
    }
}

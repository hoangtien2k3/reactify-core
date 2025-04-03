/*
 * Copyright 2024-2025 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.reactify;

import com.reactify.model.AccessToken;
import com.reactify.model.ClientResource;
import com.reactify.model.RoleDTO;
import com.reactify.model.request.*;
import com.reactify.model.response.Permission;
import java.util.List;
import java.util.Optional;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.authorization.GroupPolicyRepresentation;
import org.keycloak.representations.idm.authorization.RolePolicyRepresentation;
import reactor.core.publisher.Mono;

/**
 * Interface for interacting with Keycloak for various authentication and
 * authorization operations.
 */
public interface KeyCloakClient {

    /**
     * Retrieves an access token using the provided login request.
     *
     * @param loginRequest
     *            the login request containing user credentials
     * @return a Mono emitting an Optional containing the access token if
     *         successful, or an empty Optional if not
     */
    Mono<Optional<AccessToken>> getToken(LoginRequest loginRequest);

    /**
     * Retrieves an access token using the provided provider login request.
     *
     * @param providerLogin
     *            the provider login request containing user credentials
     * @return a Mono emitting an Optional containing the access token if
     *         successful, or an empty Optional if not
     */
    Mono<Optional<AccessToken>> getToken(ProviderLogin providerLogin);

    /**
     * Retrieves an access token using the provided client login request.
     *
     * @param clientLogin
     *            the client login request containing client credentials
     * @return a Mono emitting an Optional containing the access token if
     *         successful, or an empty Optional if not
     */
    Mono<Optional<AccessToken>> getToken(ClientLogin clientLogin);

    /**
     * Retrieves an access token for synchronization using the provided login
     * request.
     *
     * @param loginRequestSync
     *            the login request containing user credentials for synchronization
     * @return a Mono emitting an Optional containing the access token if
     *         successful, or an empty Optional if not
     */
    Mono<Optional<AccessToken>> getToken(LoginRequestSync loginRequestSync);

    /**
     * Refreshes an access token using the provided refresh token request.
     *
     * @param refreshTokenRequest
     *            the refresh token request containing the refresh token
     * @return a Mono emitting an Optional containing the new access token if
     *         successful, or an empty Optional if not
     */
    Mono<Optional<AccessToken>> refreshToken(RefreshTokenRequest refreshTokenRequest);

    /**
     * Logs out a user using the provided logout request.
     *
     * @param logoutRequest
     *            the logout request containing user information
     * @return a Mono emitting a Boolean indicating whether the logout was
     *         successful
     */
    Mono<Boolean> logout(LogoutRequest logoutRequest);

    /**
     * Retrieves permissions for a given audience and token.
     *
     * @param audience
     *            the audience for which permissions are requested
     * @param token
     *            the access token
     * @return a Mono emitting a list of permissions
     */
    Mono<List<Permission>> getPermissions(String audience, String token);

    /**
     * Retrieves client resources for a given client ID and token.
     *
     * @param clientId
     *            the client ID
     * @param token
     *            the access token
     * @return a Mono emitting a list of client resources
     */
    Mono<List<ClientResource>> getClientResources(String clientId, String token);

    /**
     * Retrieves group policies for a given client ID and token.
     *
     * @param clientId
     *            the client ID
     * @param token
     *            the access token
     * @return a Mono emitting a list of group policies
     */
    Mono<List<GroupPolicyRepresentation>> getGroupPolicies(String clientId, String token);

    /**
     * Retrieves role policies for a given client ID and token.
     *
     * @param clientId
     *            the client ID
     * @param token
     *            the access token
     * @return a Mono emitting a list of role policies
     */
    Mono<List<RolePolicyRepresentation>> getRolePolicies(String clientId, String token);

    /**
     * Retrieves role names by user ID and client ID.
     *
     * @param userId
     *            the user ID
     * @param clientId
     *            the client ID
     * @param token
     *            the access token
     * @return a Mono emitting a list of role names
     */
    Mono<List<RoleDTO>> getRoleNameByUserIdAndClientId(String userId, String clientId, String token);

    /**
     * Creates a new user in Keycloak.
     *
     * @param employeeCreateRequest
     *            the request containing user details
     * @param password
     *            the password for the new user
     * @param token
     *            the access token
     * @return a Mono emitting the ID of the created user
     */
    Mono<String> createUser(EmployeeCreateRequest employeeCreateRequest, String password, String token);

    /**
     * Creates roles for a user in Keycloak.
     *
     * @param roleUserKeycloakRequests
     *            the list of role creation requests
     * @param userId
     *            the user ID
     * @param clientId
     *            the client ID
     * @param token
     *            the access token
     * @return a Mono emitting a Boolean indicating whether the roles were
     *         successfully created
     */
    Mono<Boolean> createRoleUser(
            List<CreateRoleUserKeycloakRequest> roleUserKeycloakRequests, String userId, String clientId, String token);

    /**
     * Updates a user in Keycloak.
     *
     * @param request
     *            the request containing updated user details
     * @param token
     *            the access token
     * @return a Mono emitting a Boolean indicating whether the user was
     *         successfully updated
     */
    Mono<Boolean> updateUser(UpdateUserKeycloakRequest request, String token);

    /**
     * Removes a group from a user in Keycloak.
     *
     * @param groupId
     *            the group ID
     * @param userId
     *            the user ID
     * @param token
     *            the access token
     * @return a Mono emitting a Boolean indicating whether the group was
     *         successfully removed
     */
    Mono<Boolean> removeGroupToUser(String groupId, String userId, String token);

    /**
     * Adds a group to a user in Keycloak.
     *
     * @param groupId
     *            the group ID
     * @param userId
     *            the user ID
     * @param token
     *            the access token
     * @return a Mono emitting a Boolean indicating whether the group was
     *         successfully added
     */
    Mono<Boolean> addGroupToUser(String groupId, String userId, String token);

    /**
     * Removes roles from a user in Keycloak.
     *
     * @param roleUserKeycloakRequests
     *            the list of role removal requests
     * @param userId
     *            the user ID
     * @param clientId
     *            the client ID
     * @param token
     *            the access token
     * @return a Mono emitting a Boolean indicating whether the roles were
     *         successfully removed
     */
    Mono<Boolean> removeRoleUser(
            List<CreateRoleUserKeycloakRequest> roleUserKeycloakRequests, String userId, String clientId, String token);

    /**
     * Retrieves a user from Keycloak by user ID.
     *
     * @param userId
     *            the user ID
     * @param token
     *            the access token
     * @return a Mono emitting the user representation
     */
    Mono<UserRepresentation> getUser(String userId, String token);

    /**
     * Retrieves resources by client ID from Keycloak.
     *
     * @param clientId
     *            the client ID
     * @param token
     *            the access token
     * @return a Mono emitting a list of resource IDs
     */
    Mono<List<String>> getResourcesByClient(String clientId, String token);

    /**
     * Retrieves the group name by group ID from Keycloak.
     *
     * @param groupId
     *            the group ID
     * @param token
     *            the access token
     * @return a Mono emitting the group name
     */
    Mono<String> getGroupNameById(String groupId, String token);

    /**
     * Adds a role for a user in a specific client in Keycloak.
     *
     * @param clientId
     *            the client ID
     * @param token
     *            the access token
     * @param roleRepresentation
     *            the role representation
     * @param userId
     *            the user ID
     * @return a Mono emitting a Boolean indicating whether the role was
     *         successfully added
     */
    Mono<Boolean> addRoleForUserInClientId(
            String clientId, String token, RoleRepresentation roleRepresentation, String userId);
}

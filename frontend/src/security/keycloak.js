import Keycloak from 'keycloak-js';

const keycloakConfig = {
  url: 'http://keycloak:8080/',
  realm: 'song-microservice',
  clientId: 'frontend',
};

const keycloakService = Keycloak(keycloakConfig);

export default keycloakService;
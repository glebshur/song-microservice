import axios from "axios";
import keycloakService from "@/security/keycloak";

const http = axios.create({
    baseURL: process.env.VUE_APP_SONG_API_URL
})

http.interceptors.request.use(async (config) => {
    try {
        await keycloakService.updateToken(15);
    } catch (error) {
        console.error('Cannot update token:', error);
    }

    let token = keycloakService.token;
    if(token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
})

export default http;
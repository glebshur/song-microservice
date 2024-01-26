import { mount } from '@vue/test-utils';
import Header from '@/components/Header';
import keycloakService from '@/security/keycloak';

describe('Header.vue', () => {

    it('Displays the "Login" button when not authenticated', () => {
        const wrapper = mount(Header, {
            data() {
                return {
                    authenticated: false
                };
            }
        });

        expect(wrapper.find('.btn-outline-success').exists()).toBe(true);
        expect(wrapper.find('.btn-outline-danger').exists()).toBe(false);
    }),

    it('Doesn\'t display the "Login" button when authenticated', () => {
        const wrapper = mount(Header, {
            data() {
                return {
                    authenticated: true
                };
            }
        });

        expect(wrapper.find('.btn-outline-success').exists()).toBe(false);
        expect(wrapper.find('.btn-outline-danger').exists()).toBe(true);
    }),

    it('Displays the "Upload" and "My Songs" links when having admin role', () => {
        jest.spyOn(keycloakService, 'hasResourceRole').mockReturnValue(true);
    
        const wrapper = mount(Header);
    
        expect(wrapper.find('a[href="/song/upload"]').exists()).toBe(true);
        expect(wrapper.find('a[href="/mysongs"]').exists()).toBe(true);

        jest.spyOn(keycloakService, 'hasResourceRole').mockRestore();
    });

    it('Doesn\'t display the "Upload" and "My Songs" links when not having admin role', () => {
        jest.spyOn(keycloakService, 'hasResourceRole').mockReturnValue(false);
    
        const wrapper = mount(Header);
    
        expect(wrapper.find('a[href="/song/upload"]').exists()).toBe(false);
        expect(wrapper.find('a[href="/mysongs"]').exists()).toBe(false);

        jest.spyOn(keycloakService, 'hasResourceRole').mockRestore();
    });
})
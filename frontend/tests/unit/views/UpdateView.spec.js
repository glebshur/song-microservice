import { mount } from '@vue/test-utils';
import UpdateView from '@/views/UpdateView';
import Vuex from 'vuex';
import http from '@/api';

jest.mock('@/api', () => ({
    put: jest.fn(() => Promise.resolve())
}))

describe('UpdateView.vue', () => {
    let actions;
    let store;
    let song;
    let route;
    let router;

    beforeEach(() => {
        song = {
            id: 1,
            name: 'Test Name',
            artist: 'Test Artist',
            artistLink: 'https://test.artist.com',
            album: 'Test Album',
            albumLink: 'https://test.album.com',
            duration: 210000,
            releaseDate: '01-01-2001',
            imageLink: 'https://test.com/image.jpg'
        }
        actions = {
            fetchSingleSong: jest.fn().mockReturnValue({
                data: song
            })
        };
        store = new Vuex.Store({
            actions
        });
        route = {
            params: {
                id: 1
            }
        };
        router = {
            push: jest.fn()
        }
    });

    it('Displays error when name field is empty', async () => {
        const wrapper = mount(UpdateView, {
            global: {
                plugins: [store],
                mocks: {
                    $route: route
                }
            }
        });
        song.name = '';
        wrapper.setData({ song });

        await wrapper.vm.$nextTick()
        wrapper.vm.updateSong();

        await wrapper.vm.$nextTick()
        expect(wrapper.findComponent({ name: 'ErrorBlock'}).text()).toBeTruthy();
    }),
    it('Displays error when artist field is empty', async () => {
        const wrapper = mount(UpdateView, {
            global: {
                plugins: [store],
                mocks: {
                    $route: route
                }
            }
        });
        song.artist = '';
        wrapper.setData({ song });

        await wrapper.vm.$nextTick()
        wrapper.vm.updateSong();

        await wrapper.vm.$nextTick()
        expect(wrapper.findComponent({ name: 'ErrorBlock'}).text()).toBeTruthy();
    }),
    it('Displays error when releaseDate field is null', async () => {
        const wrapper = mount(UpdateView, {
            global: {
                plugins: [store],
                mocks: {
                    $route: route
                }
            }
        });
        song.releaseDate = null;
        wrapper.setData({ song });

        await wrapper.vm.$nextTick()
        wrapper.vm.updateSong();

        await wrapper.vm.$nextTick()
        expect(wrapper.findComponent({ name: 'ErrorBlock'}).text()).toBeTruthy();
    }),
    it('Calls http.put and router.push methods on updateSong when there\'s no errors', async () => {
        const wrapper = mount(UpdateView, {
            global: {
                plugins: [store],
                mocks: {
                    $route: route,
                    $router: router
                }
            }
        });
        wrapper.setData({ song });


        await wrapper.vm.$nextTick()
        wrapper.vm.updateSong();

        await wrapper.vm.$nextTick()
        expect(http.put).toHaveBeenCalled();
        expect(router.push).toHaveBeenCalled();
    })
});
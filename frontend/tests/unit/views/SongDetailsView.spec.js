import { shallowMount } from '@vue/test-utils';
import SongDetailsView from '@/views/SongDetailsView';
import Vuex from 'vuex';

describe('SongDetailsView.vue', () => {
    let actions;
    let store;
    let route;
    let song;

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
    });

    it('Displays the correct duration in minutes and seconds', () => {
        const wrapper = shallowMount(SongDetailsView, {
            global: {
                plugins: [store],
                mocks: {
                    $route: route
                }
            }
        });
        wrapper.setData({ song })

        wrapper.vm.$nextTick(() => {
            expect(wrapper.find('#duration').text()).toBe('3:30');
        })
    }),
    it('Renders the Download button when user has user role', () => {
        const wrapper = shallowMount(SongDetailsView, {
            global: {
                plugins: [store],
                mocks: {
                    $route: route
                }
            }
        });
        wrapper.setData({ song })
        wrapper.vm.hasUserRole = jest.fn().mockReturnValue(true);

        wrapper.vm.$nextTick(() => {
            expect(wrapper.find('#downloadButton').exists()).toBe(true);
        })
    }),
    it('Renders the Update and Delete buttons when user has admin role', () => {
        const wrapper = shallowMount(SongDetailsView, {
            global: {
                plugins: [store],
                mocks: {
                    $route: route
                }
            }
        });
        wrapper.setData({ song })
        wrapper.vm.hasAdminRole = jest.fn().mockReturnValue(true);

        wrapper.vm.$nextTick(() => {
            expect(wrapper.find('#updateButton').exists()).toBe(true);
            expect(wrapper.find('#deleteButton').exists()).toBe(true);
        })
    }),
    it('Calls "download" method when the Download button is clicked', () => {
        const wrapper = shallowMount(SongDetailsView, {
            global: {
                plugins: [store],
                mocks: {
                    $route: route
                }
            }
        });
        wrapper.setData({ song })
        wrapper.vm.hasUserRole = jest.fn().mockReturnValue(true);
        wrapper.vm.download = jest.fn();

        wrapper.vm.$nextTick(() => {
            wrapper.find('#downloadButton').trigger('click');
            expect(wrapper.vm.download).toHaveBeenCalled();
        })
    }),
    it('Calls "redirectToUpdate" method when the Update button is clicked', () => {
        const wrapper = shallowMount(SongDetailsView, {
            global: {
                plugins: [store],
                mocks: {
                    $route: route
                }
            }
        });
        wrapper.setData({ song })
        wrapper.vm.hasAdminRole = jest.fn().mockReturnValue(true);
        wrapper.vm.redirectToUpdate = jest.fn();

        wrapper.vm.$nextTick(() => {
            wrapper.find('#updateButton').trigger('click');
            expect(wrapper.vm.redirectToUpdate).toHaveBeenCalled();
        })
    }),
    it('Calls "deleteSong" method when the Delete button is clicked', () => {
        const wrapper = shallowMount(SongDetailsView, {
            global: {
                plugins: [store],
                mocks: {
                    $route: route
                }
            }
        });
        wrapper.setData({ song })
        wrapper.vm.hasAdminRole = jest.fn().mockReturnValue(true);
        wrapper.vm.deleteSong = jest.fn();

        wrapper.vm.$nextTick(() => {
            wrapper.find('#deleteButton').trigger('click');
            expect(wrapper.vm.deleteSong).toHaveBeenCalled();
        })
    });
});
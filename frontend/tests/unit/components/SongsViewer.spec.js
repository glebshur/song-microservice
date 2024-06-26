import { mount } from '@vue/test-utils';
import SongsViewer from '@/components/SongsViewer';
import Vuex from 'vuex';
import { languages, defaultLocale } from '@/i18n'
import { createI18n } from 'vue-i18n'

describe('SongsViewer.vue', () => {
    let actions;
    let store;
    let i18n;

    beforeEach(() => {
        actions = {
            fetchSongs: jest.fn()
        };
        store = new Vuex.Store({
            state: {
                songs: [],
                totalSongsNumber: 3
            },
            actions
        });
        i18n = createI18n({
            legacy: false,
            locale: defaultLocale,
            messages: Object.assign(languages)
        });
    })

    it('Calls the updateSongs method when Enter key is pressed in any input field', () => {
        const wrapper = mount(SongsViewer, {
            props: {
                songsPerPage: 3,
            },
            global: {
                plugins: [store, i18n]
            }
        });
        const updateSongsMock = jest.spyOn(wrapper.vm, 'updateSongs');

        // Simulate Enter key press in each input field
        const inputFields = wrapper.findAll('input[type="text"]');        
        inputFields.forEach((input) => {
            input.trigger('keyup.enter');
        });

        expect(updateSongsMock).toHaveBeenCalledTimes(inputFields.length);
        updateSongsMock.mockRestore();
    }),
    it('Calls the updateSongs methon when Search button is clicked', () => {
        const wrapper = mount(SongsViewer, {
            props: {
                songsPerPage: 3,
            },
            global: {
                plugins: [store, i18n]
            }
        });
        const updateSongsMock = jest.spyOn(wrapper.vm, 'updateSongs');

        wrapper.find('#searchButton').trigger('click');

        expect(updateSongsMock).toHaveBeenCalled();
        updateSongsMock.mockRestore();
    }),
    it('Disables the Previous button, when current page is 0', () => {
        const wrapper = mount(SongsViewer, {
            props: {
                songsPerPage: 3,
            },
            global: {
                plugins: [store, i18n]
            }
        });

        // console.log(wrapper.html())

        expect(wrapper.find('#prevButton').attributes('disabled')).toBe('');
    }),
    it('Disables the Next button, when there\'s no more songs', () => {
        const wrapper = mount(SongsViewer, {
            props: {
                songsPerPage: 3,
            },
            global: {
                plugins: [store, i18n]
            }
        });

        // console.log(wrapper.html())

        expect(wrapper.find('#nextButton').attributes('disabled')).toBe('');
    }),
    it('Enables the Next button, when current there\'s more songs', () => {
        let testStore = new Vuex.Store({
            state: {
                songs: [
                    {id: 1, name: 'Test'},
                    {id: 2, name: 'Test'},
                    {id: 3, name: 'Test'},
                    {id: 4, name: 'Test'},
                ],
                totalSongsNumber: 4
            },
            actions
        })

        const wrapper = mount(SongsViewer, {
            props: {
                songsPerPage: 3,
            },
            global: {
                plugins: [testStore, i18n]
            }
        });

        expect(wrapper.find('#nextButton').attributes('disabled')).toBeUndefined();
    }),
    it('Displays error message when there\'s no songs', () => {
        const wrapper = mount(SongsViewer, {
            props: {
                songsPerPage: 3,
            },
            global: {
                plugins: [store, i18n]
            }
        });

        expect(wrapper.findComponent({ name: 'ErrorBlock' }).text()).toBeTruthy();
    }),
    it('Doesn\' display error message when there\'s songs', () => {
        let testStore = new Vuex.Store({
            state: {
                songs: [
                    {id: 1, name: 'Test'},
                    {id: 2, name: 'Test'},
                ]
            },
            actions
        })

        const wrapper = mount(SongsViewer, {
            props: {
                songsPerPage: 3,
            },
            global: {
                plugins: [testStore, i18n]
            }
        });

        expect(wrapper.findComponent({ name: 'ErrorBlock' }).text()).not.toBeTruthy();
    })

});

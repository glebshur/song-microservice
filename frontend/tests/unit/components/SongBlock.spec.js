import { mount } from '@vue/test-utils';
import SongBlock from '@/components/SongBlock';
import ImageSelector from "@/imageSelector/imageSelector";
import { createRouter, createWebHistory } from 'vue-router';

describe('SongBlock.vue', () => {
    it('Use the image url from image selector when song.imageLink is null', () => {
        const song = {
            id: 1,
            name: 'Name',
            artist: 'Artist',
            imageLink: null,
        };
        const randomLink = 'https://test.com/image.jpg';
        jest.spyOn(ImageSelector, 'getRandomImage').mockReturnValue(randomLink);

        const wrapper = mount(SongBlock, {
            props: {
                song,
            }
        });

        const imgElement = wrapper.find('.card-img-top');
        expect(imgElement.attributes('src')).toBe(randomLink);

        jest.spyOn(ImageSelector, 'getRandomImage').mockRestore();
    }),
    it('Use the image url from song.imageLink when it is not null', () => {
        const song = {
            id: 1,
            name: 'Name',
            artist: 'Artist',
            imageLink: 'https://test.com/image.jpg',
        };

        const wrapper = mount(SongBlock, {
            props: {
                song,
            }
        });

        const imgElement = wrapper.find('.card-img-top');
        expect(imgElement.attributes('src')).toBe(song.imageLink);
    }),
    it('Calls the detailedInfo method when the card is clicked', async () => {
        const song = {
          id: 1,
          name: 'Song Name',
          artist: 'Artist Name',
          imageLink: 'https://test.com/image.jpg',
        };
        const mockRouter = createRouter({
            history: createWebHistory(),
            routes: [
              {
                name: 'SongDetails',
                path: '/song/:id',
                component: {
                  template: '<div>Song Details</div>',
                },
              },
            ],
          });
        const wrapper = mount(SongBlock, {
            propsData: {
                song,
            },
            global: {
                plugins: [mockRouter],
            },
        });

        const detailedInfoMock = jest.spyOn(wrapper.vm, 'detailedInfo');
        await wrapper.find('.card').trigger('click');

        expect(detailedInfoMock).toHaveBeenCalled();

        detailedInfoMock.mockRestore();
    });
})
import { mount } from '@vue/test-utils';
import UploadView from '@/views/UploadView';
import http from '@/api';
import { languages, defaultLocale } from '@/i18n'
import { createI18n } from 'vue-i18n'

jest.mock('@/api', () => ({
    post: jest.fn(() => Promise.resolve())
}))

describe('UploadView.vue', () => {
    let i18n;

    beforeEach(() => {
        i18n = createI18n({
            legacy: false,
            locale: defaultLocale,
            messages: Object.assign(languages)
        });
    });

    it('Displays an error when the file has an incorrect extension', async () => {
        const wrapper = mount(UploadView, {
            global: {
                plugins: [i18n]
            }
        });
        const file = new File([''], 'test.txt', { type: 'text/plain' });

        wrapper.vm.setFile(file);

        await wrapper.vm.$nextTick();
        expect(wrapper.findComponent({name : 'ErrorBlock'})).toBeTruthy();
        expect(wrapper.vm.file).toBe(null);
    })
    it('Displays an error when the file exceeds the maximum allowed size', async () => {
        const wrapper = mount(UploadView, {
            global: {
                plugins: [i18n]
            }
        });
        const fileSize = (wrapper.vm.maxSize + 10) * 1048576;
        const file = { name: 'test.mp3', size: fileSize};

        wrapper.vm.setFile(file);

        await wrapper.vm.$nextTick();
        expect(wrapper.findComponent({name : 'ErrorBlock'})).toBeTruthy();
        expect(wrapper.vm.file).toBe(null);
    })
    it('Sets file when there\'s no errors', () => {
        const wrapper = mount(UploadView, {
            global: {
                plugins: [i18n]
            }
        });
        const file = new File([''], 'test.mp3', { type: 'audio/mpeg' });

        wrapper.vm.setFile(file);

        expect(wrapper.vm.file).toBe(file);
    })
    it('Uploads file when there\'s file', async () => {
        const wrapper = mount(UploadView, {
            global: {
                plugins: [i18n]
            }
        });
        const file = new File([''], 'test.mp3', { type: 'audio/mpeg' });
        wrapper.vm.file = file

        wrapper.vm.uploadFile()

        await wrapper.vm.$nextTick();
        expect(http.post).toHaveBeenCalled();
        await wrapper.vm.$nextTick();
        expect(wrapper.findComponent({name : 'InfoBlock'})).toBeTruthy();
    })
    it('Shows error when there\'s no file', async () => {
        const wrapper = mount(UploadView, {
            global: {
                plugins: [i18n]
            }
        });
        wrapper.vm.file = null

        wrapper.vm.uploadFile()

        await wrapper.vm.$nextTick();
        expect(wrapper.findComponent({name : 'ErrorBlock'})).toBeTruthy();
    })
    it('Calls handleDrop when drop event is triggered', () => {
        const wrapper = mount(UploadView, {
            global: {
                plugins: [i18n]
            }
        });
        wrapper.vm.handleDrop = jest.fn();

        wrapper.find('#fileDropZone').trigger('drop');

        expect(wrapper.vm.handleDrop).toHaveBeenCalled();
        wrapper.vm.handleDrop.mockRestore();
    })
    it('Calls setActive when dragover event is triggered', () => {
        const wrapper = mount(UploadView, {
            global: {
                plugins: [i18n]
            }
        });
        wrapper.vm.setActive = jest.fn();

        wrapper.find('#fileDropZone').trigger('dragover');

        expect(wrapper.vm.setActive).toHaveBeenCalled();
        wrapper.vm.setActive.mockRestore();
    })
    it('Calls setInactive when dragleave event is triggered', () => {
        const wrapper = mount(UploadView, {
            global: {
                plugins: [i18n]
            }
        });
        wrapper.vm.setInactive = jest.fn();

        wrapper.find('#fileDropZone').trigger('dragleave');

        expect(wrapper.vm.setInactive).toHaveBeenCalled();
        wrapper.vm.setInactive.mockRestore();
    })
    it('Calls handleClick when click event is triggered', () => {
        const wrapper = mount(UploadView, {
            global: {
                plugins: [i18n]
            }
        });
        wrapper.vm.handleClick = jest.fn();

        wrapper.find('#fileDropZone').trigger('click');

        expect(wrapper.vm.handleClick).toHaveBeenCalled();
        wrapper.vm.handleClick.mockRestore();
    })
    it('Calls handleInputChange when change event on input is triggered', () => {
        const wrapper = mount(UploadView, {
            global: {
                plugins: [i18n]
            }
        });
        wrapper.vm.handleInputChange = jest.fn();

        wrapper.find('#fileChooser').trigger('change');

        expect(wrapper.vm.handleInputChange).toHaveBeenCalled();
        wrapper.vm.handleInputChange.mockRestore();
    })
    it('Calls uploadSong when click event on button is triggered', () => {
        const wrapper = mount(UploadView, {
            global: {
                plugins: [i18n]
            }
        });
        wrapper.vm.uploadFile = jest.fn();

        wrapper.find('#uploadButton').trigger('click');

        expect(wrapper.vm.uploadFile).toHaveBeenCalled();
        wrapper.vm.uploadFile.mockRestore();
    });
});
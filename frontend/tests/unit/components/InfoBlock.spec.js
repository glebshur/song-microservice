import { mount } from '@vue/test-utils';
import InfoBlock from '@/components/InfoBlock';

describe('InfoBlock.vue', () => {
    it('Doesn\'t render message when it doesn\'t exist', () => {
        const wrapper = mount(InfoBlock);

        expect(wrapper.find('div').exists()).not.toBeTruthy();
    }),

    it('Renders message when it exists', () => {
        const wrapper = mount(InfoBlock, {
            props: {
                message: 'Test'
            }
        });

        if(expect(wrapper.find('div').exists()).toBeTruthy()) {
            expect(wrapper.text()).toBe('Test');
        }
    })
})
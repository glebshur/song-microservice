import { mount } from '@vue/test-utils'
import ErrorBlock from '@/components/ErrorBlock'

describe('ErrorBlock.vue', () => {

    it('Don\'t render errors if they don\'t exist', () => {
        const wrapper = mount(ErrorBlock, {
            props: {
                errors: null
            }
        });

        expect(wrapper.find('div').exists()).not.toBeTruthy();
    }),

    it('Renders errors if they exists', () => {
        const wrapper = mount(ErrorBlock, {
            props: {
                errors: ['Error 1']
            }
        });

        if(expect(wrapper.find('div').exists()).toBeTruthy()) {
            expect(wrapper.text()).toBe('Error 1');
        }
    })
})
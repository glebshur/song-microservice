<template>
    <Header />
    <div class="bg-image">
        <div class="d-flex justify-content-center p-3">
            <div class="bg-danger bg-opacity-50 text-light p-3 col-sm-10 col-md-8 col-lg-7 col-xl-6 col-xxl-4 rounded">
                <img src="/icons/sadFace.svg" width="100" height="100">
                <h3 class="mt-2">{{$t(getErrorMessageCode(errorCode))}}</h3>
            </div>
        </div>
    </div>
</template>
<script>
import Header from '@/components/Header.vue';
import { useI18n } from 'vue-i18n';
import errorMessageCodes from '@/errors/errorMessageCodes.json'

export default {
    name: 'error-view',
    setup() {
    const { t } = useI18n({useScope: 'global'});
        return { t }
    },
    components: {
        Header
    },
    props: {
        errorCode: {
            type: String,
            required: true
        }
    },
    methods: {
        getErrorMessageCode(errorCode) {
        return errorMessageCodes[errorCode] || 'errorPage.messages.default';
        }
    }
}
</script>
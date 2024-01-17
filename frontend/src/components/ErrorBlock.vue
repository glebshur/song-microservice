<template>
    <div v-if="errors" class="d-flex justify-content-center p-3">
        <div class="bg-danger bg-opacity-50 text-light p-3 col-sm-10 col-md-8 col-lg-7 col-xl-6 col-xxl-4 rounded">
            <div v-for="error in errors" :key="error">{{ error }}</div>
        </div>
    </div>
</template>
<script>
export default {
    name: 'error-block',
    props: {
        errors: {
            type: Array,
        }
    }
}
</script>
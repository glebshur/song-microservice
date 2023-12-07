<template>
    <div v-if="errors">
        <div v-for="error in errors" :key="error">{{ error }}</div>
    </div>
</template>
<script>
export default {
    name: 'error-block',
    props: {
        errors: {
            type: Array,
            required: true
        }
    }
}
</script>
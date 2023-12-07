<template>
    <div v-if="message">{{ message }}</div>
</template>
<script>
export default {
    name: 'info-block',
    props: {
        message: {
            type: String,
            required: true
        }
    }
}
</script>

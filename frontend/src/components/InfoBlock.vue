<template>
    <div v-if="message" class="d-flex justify-content-center p-3">
        <div class="bg-info bg-opacity-50 text-light p-3 col-sm-8 col-md-6 col-lg-4 col-xl-3 col-xxl-2 rounded">
            <p>{{message}}</p>  
        </div>
    </div>
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

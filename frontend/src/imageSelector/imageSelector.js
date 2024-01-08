const ImageSelector = {
    imagePaths: ["https://thumbs2.imgbox.com/53/a3/6llCKSpi_t.jpg",
    "https://thumbs2.imgbox.com/20/d2/Lazn8Mtp_t.jpg",
    "https://thumbs2.imgbox.com/53/a3/6llCKSpi_t.jpg",
    "https://thumbs2.imgbox.com/d3/d4/h0ke4a60_t.jpg",
    "https://thumbs2.imgbox.com/b4/28/GBjA8aaz_t.jpg",
    "https://thumbs2.imgbox.com/7f/5c/Mr0Q8RuA_t.jpg"],
    

    getRandomImage() {
        const randomIndex = Math.floor(Math.random() * this.imagePaths.length);
        return this.imagePaths[randomIndex];
    }
};

export default ImageSelector;
const TimeFormatter = {
    formatTime(totalSeconds) {
        var minutes = Math.floor(totalSeconds / 60);
        var seconds = (totalSeconds % 60).toFixed(0);
        return (
          seconds == 60 ?
          (minutes+1) + ":00" :
          minutes + ":" + (seconds < 10 ? "0" : "") + seconds
        );
    }
};

export default TimeFormatter;
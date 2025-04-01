(function () {
    function initialize() {
        console.log('Bootstrap script loaded');
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initialize);
    } else {
        initialize();
    }
})();
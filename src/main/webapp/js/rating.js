function updateRating(bookId, value) {
    $.post({
        url: "/updateBookRating",
        data: {
            bookId: bookId,
            value: value
        },
        success: function (data) {
            alert(data);
        }
    });
}
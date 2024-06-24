
function favoriteContacts(id) {

    const deletedData = {
        userId: id,
        contacts: processDataIntoArray(selectedIds)
    };

    // Show loading alert
    Swal.fire({
        title: 'Adding to favorites...',
        text: 'Please wait while we add the Contact to your favorites.',
        icon: 'info',
        allowOutsideClick: false,
        didOpen: () => {
            Swal.showLoading();
        }
    });

    // Send favorite request to the backend
    fetch(`${baseUrl}api/makefavorite`, {

        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(deletedData)
    })
        .then(response => {

            if (response.ok) {

                // Show success alert with bookmark icon
                Swal.fire({
                    title: 'Favorited!',
                    text: 'The Contact has been added to your favorites.',
                    icon: 'success',
                    html: '<i class="fas fa-bookmark" style="color: #3085d6; font-size: 2em;"></i>',
                    allowOutsideClick: false

                });
                localStorage.removeItem("selectedIds");
                window.location.reload();
            } else {
                // Show error alert
                Swal.fire({
                    title: 'Error!',
                    text: 'There was an error adding the Contact to your favorites.',
                    icon: 'error',
                    allowOutsideClick: false
                });
            }
        })
        .catch(error => {
            // Show error alert
            Swal.fire({
                title: 'Error!',
                text: 'There was an error adding the Contact to your favorites.',
                icon: 'error',
                allowOutsideClick: false
            });
        });
};
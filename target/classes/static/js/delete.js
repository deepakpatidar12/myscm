function deleteContacts(id) {

    const deletedData = {
        userId: id,
        contacts: processDataIntoArray(selectedIds),
    };
    // Show confirmation dialog
    Swal.fire({
        title: "Are you sure?",
        text: "You won't be able to revert this!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, delete it!",
        allowOutsideClick: false,
    }).then((result) => {
        if (result.isConfirmed) {
            // Show loading alert
            Swal.fire({
                title: 'Deleting...',
                text: 'Please wait while we delete the Contact.',
                icon: 'info',
                allowOutsideClick: false,
                didOpen: () => {
                    Swal.showLoading();
                }
            });

            // Send delete request to the backend
            fetch(`${baseUrl}api/deleteContacts`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(deletedData)
            })
            .then(response => {

                if (response.ok) {
                    // Show success alert
                    Swal.fire({
                        title: 'Deleted!',
                        text: 'The Contact has been deleted.',
                        icon: 'success',
                        allowOutsideClick: false
                    });
                    localStorage.removeItem("selectedIds");
                    window.location.reload();
                } else {
                    // Show error alert
                    Swal.fire({
                        title: 'Error!',
                        text: 'There was an error deleting the Contact.',
                        icon: 'error',
                        allowOutsideClick: false
                    });
                }
            })
            .catch(error => {
                // Show error alert
                Swal.fire({
                    title: 'Error!',
                    text: 'There was an error deleting the Contact.',
                    icon: 'error',
                    allowOutsideClick: false
                });
            });
        } else if (result.dismiss === Swal.DismissReason.cancel) {
            Swal.fire({
                title: 'Cancelled',
                text: 'Your Contact is safe :)',
                icon: 'error',
                allowOutsideClick: false
            });
        }
    });
};
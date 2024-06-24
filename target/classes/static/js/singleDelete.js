function deletecontact(cid, page, fromRes, element, from, searchValue, searchField) {

    if (element == 1) {
        page -= 1;
    }

    // Show confirmation dialog
    Swal.fire({
        title: "Are you sure?",
        text: "You won't be able to revert this!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, delete it!",
        allowOutsideClick: false

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
            fetch(`${baseUrl}api/deleteSingle?contact=${cid}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify()


            })
                .then(response => {

                    if (response.ok) {
                        // Show success alert

                        Swal.fire({
                            title: 'Deleted!',
                            text: 'The Contact has been deleted.',
                            icon: 'success',
                            timer: 800,
                            html: '<i class="fas fa-bookmark" style="color: #3085d6; font-size: 2em;"></i>',
                            timerProgressBar: true,
                            didOpen: () => {
                                Swal.showLoading();
                                const timer = Swal.getPopup().querySelector("b");
                                timerInterval = setInterval(() => {
                                    timer.textContent = `${Swal.getTimerLeft()}`;
                                }, 1000);
                            },
                            icon: 'success'
                        });


                        if (from != null && from !== "") {

                            if (from.toLowerCase() === "search" && element > 1) {
                                console.log(fromRes);
                                window.location.replace(`${baseUrl}scm2/user/search?searchField=${searchField}&fromRes=${fromRes}&fieldValue=${searchValue}`)
                            }
                        } else {

                            switch (fromRes) {
                                case "view":
                                    window.location.replace(`${baseUrl}scm2/user/view-contact?page=${page}`)
                                    break;
                                case "fev":
                                    window.location.replace(`${baseUrl}scm2/user/fav-contact?page=${page}`)
                                    break;
                                default:
                                    window.location.replace(`${baseUrl}scm2/user/view-contact?page=${page}`)
                            }
                        }

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
                text: 'Your contact is safe :)',
                icon: 'error',
                allowOutsideClick: false
            });
        }
    });

}
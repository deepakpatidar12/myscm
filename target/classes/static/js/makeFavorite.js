function makeFavoritecontact(id, page, view, element, favorite, from, searchField, searchValue) {

    var fav;
    if (favorite) {
        fav = "Successfully Drop favorite! ";
    } else {
        fav = "Successfully Make favorite!"
    }

    // Show loading alert
        
        Swal.fire({
            title: 'Adding to favorites...',
            text: 'Please wait while we add the contact to your favorites.',
            icon: 'info',
            allowOutsideClick: false,
            didOpen: () => {
                Swal.showLoading();
            }
        });

    // Send favorite request to the backend
    fetch(`${baseUrl}api/favoriteSingle?contact=${id}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify()
    })
        .then(response => {

            if (response.ok) {
                // Show success alert with bookmark icon
                Swal.fire({
                    title: fav,
                    text: favorite,
                    icon: 'success',
                    html: '<i class="fas fa-bookmark" style="color: #3085d6; font-size: 2em;"></i>',
                    allowOutsideClick: false
                });

                localStorage.removeItem("selectedIds");
                if (from !== undefined) {

                    switch (from) {
                        case 'unique':
                            window.location.replace(`${baseUrl}scm2/user/unique?id=${id}&page=${page}&element=${element}&fromRes=${view}`)
                            break;

                        case 'search':
                                window.location.replace(`${baseUrl}scm2/user/search?searchField=${searchField}&fromRes=${view}&fieldValue=${searchValue}`);
                            break;

                        default:
                            window.location.replace(`${baseUrl}scm2/user/view-contact?page=${page}`);
                            break;
                    }
                } else {

                    switch (view) {
                        case 'view':
                            window.location.replace(`${baseUrl}scm2/user/view-contact?page=${page}`)
                            break;

                        case 'fev':
                            window.location.replace(`${baseUrl}scm2/user/fav-contact?page=${page}`);
                            break;

                        default:
                            window.location.replace(`${baseUrl}scm2/user/view-contact?page=${page}`);
                            break;
                    }
                }


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
            Swal.close(); // Close the loading alert
            // Show error alert
            Swal.fire({
                title: 'Error!',
                text: 'There was an error adding the Contact to your favorites.',
                icon: 'error',
                allowOutsideClick: false
            });
        });
};
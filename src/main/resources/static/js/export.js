
const selectedIds = JSON.parse(localStorage.getItem('selectedIds')) || [];
const selectedDataDiv = document.getElementById('dropdownActionButton');

if (selectedIds.length > 0) {
    selectedDataDiv.classList.remove('invisible');
}

function processDataIntoArray(data) {
    // Example: Assuming data is an array of objects with 'name' property
    return data.map(item => item);

}

document.addEventListener('DOMContentLoaded', () => {

    const checkboxes = document.getElementsByClassName('checkbox-table');

    // Update UI based on stored selectedIds
    const updateUI = () => {
        Array.from(checkboxes).forEach(checkbox => {
            const row = checkbox.closest('tr');
            const idInput = row.querySelector('.view-id');
            const contactId = idInput.value;

            const isSelected = selectedIds.includes(contactId);
            checkbox.checked = isSelected;
            if (isSelected) {
                row.classList.add('selected');
            } else {
                row.classList.remove('selected');
            }
        });
    };

    // Save selectedIds to local storage
    const saveSelectedIds = () => {
        localStorage.setItem('selectedIds', JSON.stringify(selectedIds));
    };

    // Add event listeners to checkboxes
    Array.from(checkboxes).forEach(checkbox => {
        checkbox.addEventListener('change', function () {
            const row = this.closest('tr');
            const idInput = row.querySelector('.view-id');
            const contactId = idInput.value;

            if (this.checked) {
                row.classList.add('selected');
                if (!selectedIds.includes(contactId)) {
                    selectedIds.push(contactId);

                }
            } else {
                row.classList.remove('selected');
                const index = selectedIds.indexOf(contactId);
                if (index > -1) {
                    selectedIds.splice(index, 1);
                }
            }
            saveSelectedIds();
        });
    });

    updateUI();
});


// for click the export data

// Send data to backend
function exportPDF() {


    Swal.fire({
        title: 'Generating PDF...',
        html: '<i class="fas fa-file-pdf" style="font-size: 4rem; color: red;"></i>',
        showConfirmButton: false,
        allowOutsideClick: false,
        allowEscapeKey: false,
        didOpen: () => {
            Swal.showLoading();

            // Fetch the PDF generation status from the backend
            fetch('/api/generate-pdf', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(processDataIntoArray(selectedIds))
            })
                .then(response => {
                    // Check if response is successful
                    if (response.ok) {

                        const filename = response.headers.get('Content-Disposition').split('filename=')[1];

                        return response.blob().then(blob => {
                            // Create object URL for blob
                            const url = window.URL.createObjectURL(blob);

                            Swal.fire({
                                icon: 'success',
                                title: 'PDF Generated',
                                text: 'Your PDF has been generated successfully!',
                                confirmButtonText: 'Download'
                            }).then((result) => {
                                if (result.isConfirmed) {
                                    const a = document.createElement('a');
                                    a.href = url;
                                    a.download = filename;
                                    document.body.appendChild(a);
                                    a.click();
                                    window.URL.revokeObjectURL(url);
                                    document.body.removeChild(a);
                                    // Clean up
                                    localStorage.removeItem("selectedIds");
                                    window.location.reload();
                                }
                            });


                        });

                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: 'Error',
                            text: 'There was an error generating the PDF. Please try again.',
                        });
                    }
                })
                .catch(error => {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error',
                        text: 'There was an error communicating with the server. Please try again.',
                    });
                });
        }
    });
}

// Export Excel file

function exportExcel() {

    Swal.fire({
        title: 'Generating Excel...',
        html: '<i class="fas fa-file-excel" style="font-size: 4rem; color: green;"></i>',
        showConfirmButton: false,
        allowOutsideClick: false,
        allowEscapeKey: false,
        didOpen: () => {
            Swal.showLoading();

            // Fetch the Excel generation status from the backend

            fetch('/api/generate-excel', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(processDataIntoArray(selectedIds))
            })
                .then(response => {
                    // Check if response is successful
                    if (response.ok) {

                        // Extract filename from content-disposition header
                        const filename = response.headers.get('Content-Disposition').split('filename=')[1];

                        // Return blob response
                        return response.blob().then(blob => {
                            // Create object URL for blob
                            const url = window.URL.createObjectURL(blob);

                            Swal.fire({
                                icon: 'success',
                                title: 'Excel Generated',
                                text: 'Your Excel file has been generated successfully!',
                                confirmButtonText: 'Download'
                            }).then((result) => {
                                if (result.isConfirmed) {
                                    const a = document.createElement('a');
                                    a.href = url;
                                    a.download = filename;
                                    document.body.appendChild(a);
                                    a.click();

                                    // Clean up
                                    window.URL.revokeObjectURL(url);
                                    document.body.removeChild(a);
                                    localStorage.removeItem("selectedIds");
                                    window.location.reload();
                                }
                            });
                        })
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: 'Error',
                            text: 'There was an error generating the Excel file. Please try again.',
                        });
                    }
                })
                .catch(error => {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error',
                        text: 'There was an error communicating with the server. Please try again.',
                    });
                });
        }
    });
}


//  for view the action Bar
document.addEventListener('DOMContentLoaded', () => {

    const checkboxes = document.querySelectorAll('.checkbox-table');

    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            const anyChecked = Array.from(checkboxes).some(checkbox => checkbox.checked);
            if (anyChecked) {
                selectedDataDiv.classList.remove('invisible');
            } else {
                selectedDataDiv.classList.add('invisible');
            }
        });
    });
});
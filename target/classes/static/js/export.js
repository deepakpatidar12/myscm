
const selectedIds = JSON.parse(localStorage.getItem('selectedIds')) || [];
const selectedDataDiv = document.getElementById('dropdownActionButton');

if(selectedIds.length > 0){
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

    fetch('/api/generate-pdf', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(processDataIntoArray(selectedIds))
    })
        .then(response => {
            // Check if response is successful
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            // Extract filename from content-disposition header
            const filename = response.headers.get('Content-Disposition').split('filename=')[1];

            // Return blob response
            return response.blob().then(blob => {
                // Create object URL for blob
                const url = window.URL.createObjectURL(blob);

                // Create anchor element for download
                const a = document.createElement('a');
                a.href = url;
                a.download = filename;
                document.body.appendChild(a);

                // Trigger click event on anchor element
                a.click();

                // Clean up
                window.URL.revokeObjectURL(url);
                document.body.removeChild(a);
                localStorage.removeItem("selectedIds");
                window.location.reload();

            });
        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
        });
}

// Export Excel file

function exportExcel() {

    fetch('/api/generate-excel', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(processDataIntoArray(selectedIds))
    })
        .then(response => {
            // Check if response is successful
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            // Extract filename from content-disposition header
            const filename = response.headers.get('Content-Disposition').split('filename=')[1];

            // Return blob response
            return response.blob().then(blob => {
                // Create object URL for blob
                const url = window.URL.createObjectURL(blob);

                // Create anchor element for download
                const a = document.createElement('a');
                a.href = url;
                a.download = filename;
                document.body.appendChild(a);

                // Trigger click event on anchor element
                a.click();

                // Clean up
                window.URL.revokeObjectURL(url);
                document.body.removeChild(a);
                localStorage.removeItem("selectedIds");
                window.location.reload();
            });
        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
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
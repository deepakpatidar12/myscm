// for view the model

const info_model = document.getElementById("info-modal");

const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
    },
    onShow: () => {
    },
    onToggle: () => {
    },
};

// instance options object
const instanceOptions = {
    id: "info-modal",
    override: true
};

const contactinfoModal = new Modal(info_model, options, instanceOptions);

function OpenInfoModal() {
    contactinfoModal.show();
}

function closeInfoModal() {
    contactinfoModal.hide();
}

async function loadData(contact, user) {

    try {
        const data = await (
            await fetch(`${baseUrl}api/contact-info/${contact}/${user}`)
        ).json();


        document.querySelector("#contactImg").src = data.contactPic;
        document.querySelector("#contactName").innerHTML = data.name;
        document.querySelector("#contactNickname").innerHTML = data.nickName;
        document.querySelector("#contactEmail").innerHTML = data.contactEmail;
        document.querySelector("#contactPhone").innerHTML = data.contactNumber;
        document.querySelector("#contactLinkedin").href = data.links[0].linkedIn;
        document.querySelector("#contactInstagram").href = data.links[0].instagram;
        document.querySelector("#contactAddress").innerHTML = data.address;
        document.querySelector("#contactAbout").innerHTML = data.about;

        OpenInfoModal();
    } catch (error) {

        let timerInterval;
        Swal.fire({
            title: " Something went wrong",
            html: "try after some time later!! ",
            timer: 1000,
            timerProgressBar: true,

            willClose: () => {
                clearInterval(timerInterval);

                 window.location.reload();
            }
        }).then((result) => {
            if (result.dismiss === Swal.DismissReason.timer) {
            }
        });
    }

}




const baseUrl = "http://localhost:8081/";

// This is For Chnage Thyme 

changelogo();

let currentThyme = getThyme();

const changeThymeBtn = document.querySelector('.thyme-change-btn');

document.addEventListener("DOMContentLoaded", () => {

    changeThyme(currentThyme);
})

function changeThyme(currentThyme) {

    // set to web page

    document.querySelector('html').classList.add(currentThyme);

    // create the listener to change the thyme

    changeThymeBtn.querySelector('span').textContent = currentThyme == "dark" ? ": Light" : ": Dark";

    changeThymeBtn.addEventListener("click", () => {
        let oldThyme = currentThyme;

        if (currentThyme == "dark") {

            //set Thyme Light
            currentThyme = "light";
        } else {

            //set Thyme Dark
            currentThyme = "dark";
        }
        changePageThyme(currentThyme, oldThyme);

    })

}

function changePageThyme(newthyme, oldthyme) {

    // update The LocalStorage

    document.querySelector('html').classList.remove(oldthyme);
    setThyme(newthyme);
    document.querySelector('html').classList.add(newthyme);
    changeThymeBtn.querySelector('span').textContent = newthyme == "dark" ? ": Light" : ": Dark";

}

// set Thyme to localStorage
function setThyme(thyme) {
    localStorage.setItem("thyme", thyme);
    changelogo();
}

// Get Thyme From LocalStorage
function getThyme() {
    let thyme = localStorage.getItem("thyme");
    return thyme ? thyme : "light";
}

function changelogo(){
    inlogo = getThyme();
    
    
    if(inlogo == "light"){
        document.querySelector(".mylogo").src = "../../img/whitescm.png";
        window.location.pathname == "/scm2/about" ? document.querySelector(".picture").src = "../../img/picture1.png" : '';
        
    }else{
        document.querySelector(".mylogo").src = "../../img/blackscm.png";
        window.location.pathname == "/scm2/about" ? document.querySelector(".picture").src = "../../img/picture2.png" : '';
    }
}

// This is End of Thyme Change Code

// It is for browser Back button

// This is for Sweet alert delete Contact

// function deletecon(cid, page, fromRes, element, from, searchValue, searchField) {


//     Swal.fire({
//         title: "Are you sure?",
//         text: "You won't be able to revert this!",
//         icon: "warning",
//         showCancelButton: true,
//         confirmButtonColor: "#3085d6",
//         cancelButtonColor: "#d33",
//         confirmButtonText: "Yes, delete it!"
//     }).then((result) => {
//         if (result.isConfirmed) {


//             if (from === "search") {
//                 url = `${baseUrl}scm2/user/delete-contact?contact=${cid}&page=${page}&from=${from}&element=${element}&fromRes=${fromRes}&fieldValue=${searchValue}&searchField=${searchField}`;
//             } else {

//                 url = `${baseUrl}scm2/user/delete-contact?page=${page}&contact=${cid}&fromRes=${fromRes}&element=${element}`;
//             }

//             window.location.replace(url);

//             Swal.fire({
//                 title: "Deleted!",
//                 text: "Your file has been deleted.",
//                 icon: "success"
//             });

//         }
//     });

// }

// // for make favorite
// function makeFavorite(contact, page, view, element, favorite, from, searchField, searchValue) {


//     var fav;
//     if (favorite) {
//         fav = "Successfully Drop favorite! ";
//     } else {
//         fav = "Successfully Make favorite!"
//     }

//     let timerInterval;
//     Swal.fire({
//         title: fav,
//         icon: "info",
//         timer: 800,
//         timerProgressBar: true,
//         didOpen: () => {
//             Swal.showLoading();
//             const timer = Swal.getPopup().querySelector("b");
//             timerInterval = setInterval(() => {
//                 timer.textContent = `${Swal.getTimerLeft()}`;
//             }, 100);
            
//             if (from == "search") {

//                 url = `${baseUrl}scm2/user/fev?contact=${contact}&from=${from}&element=${element}&fromRes=${view}&searchField=${searchField}&fieldValue=${searchValue}`;
//             } else if (from == "unique") {

//                 url = `${baseUrl}scm2/user/unique-fev?page=${page}&contact=${contact}&fromRes=${view}&element= ${element}`;
//             } else {

//                 url = `${baseUrl}scm2/user/fev?page=${page}&contact=${contact}&fromRes=${view}&element=${element}`;
//             }

//             window.location.replace(url);
//         },
//         willClose: () => {
//             clearInterval(timerInterval);
//         }
//     }).then((result) => {
//         /* Read more about handling dismissals below */
//         if (result.dismiss === Swal.DismissReason.timer) {
//         }
//     });

// }



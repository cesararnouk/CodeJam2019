const serverurl = "http://localhost:8080/CodeJam2019/MyServlet";

document.addEventListener('DOMContentLoaded', () => {
    console.log("Loaded...");
    renderProfs();
});

async function renderProfs(){
    // Query the server to send the prof list, await response (array, hopefully)
    let x = await queryProfs();
    let profs = JSON.parse(x);
    console.log(profs);
    // For each prof create an option in the select
    profs.forEach(prof => {
        console.log(prof);
        let option = document.createElement("option");
        option.value = prof.id;
        option.innerHTML = `${prof.first_name} ${prof.last_name}`;
        document.getElementById("prof-list").appendChild(option);
    });
}

document.getElementById("submit").onclick = function(){
	console.log("Submitting...");
    let text = document.getElementById('textbox').value;
    let list = document.getElementById('prof-list');
    let prof = list.options[list.selectedIndex].value;

    if (text == "" || prof == ""){
    	alert("Please input a professor and a message.");
    	console.log("Submit cancelled");
    	return;
    }
    
    let XHR = new XMLHttpRequest();
    
    let data = "id=" + encodeURIComponent(prof) + "text=" + encodeURIComponent(text);

    // Define what happens on successful data submission
    XHR.addEventListener('load', function(event) {
        console.log('Yeah! Data sent and response loaded.');
        document.getElementById('textbox').value = "";
        document.getElementById('prof-list').selectedIndex = 0;
    });

    // Define what happens in case of error
    XHR.addEventListener('error', function(event) {
        console.log('Oops! Something goes wrong.');
    });

    // Set up our request
    XHR.open('POST', serverurl, true);

    // Add the required HTTP header for form data POST requests
    XHR.setRequestHeader("action", "REVIEW");
    XHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

    // Finally, send our data.
    XHR.send(data);
    
    
}

function queryProfs(){
    return new Promise(function (resolve, reject) {
        let XHR = new XMLHttpRequest();
        // Set up our request
        XHR.open('POST', serverurl, true);
        XHR.setRequestHeader('Accept', 'application/json');
        XHR.setRequestHeader("action", "PROFS");
        let params = 'action=' + encodeURIComponent("PROFS");
        
        XHR.onreadystatechange = () => {
            if (XHR.readyState === XMLHttpRequest.DONE) {
                if (XHR.status === 200) {
                    resolve(XHR.responseText);
                } else {
                	reject(XHR.status);
                }
            }
        }
        XHR.onerror = () => reject(XHR.statusText);
        XHR.send();
    });
}
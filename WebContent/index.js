const serverurl = "http://localhost:8080/CodeJam2019/MyServlet";

const profsDB = [{id:1, name:'Charles Broth'}, {id:2, name:'Frank Jerrie'}, {id:3, name:'Donald Pavis'}];

document.addEventListener('DOMContentLoaded', () => {
    console.log("Loaded...");
    renderProfs();
});

async function renderProfs(){
    // Query the server to send the prof list, await response (array, hopefully)
    //let profs = await queryProfs();
    let profs = profsDB;
    // For each prof create a 
    profs.forEach(prof => {
        console.log(prof);
        let option = document.createElement("option");
        option.value = prof.id;
        option.innerHTML = `${prof.name}`;
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
    });

    // Define what happens in case of error
    XHR.addEventListener('error', function(event) {
        console.log('Oops! Something goes wrong.');
    });

    // Set up our request
    XHR.open('POST', serverurl, true);

    // Add the required HTTP header for form data POST requests
    XHR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

    // Finally, send our data.
    XHR.send(data);
}

function queryProfs(){
    return new Promise(function (resolve, reject) {
        var XHR = new XMLHttpRequest();
        // Set up our request
        XHR.open('GET', serverurl+'/getprofs', true);
        XHR.onreadystatechange = () => {
            if (httpRequest.readyState === XMLHttpRequest.DONE) {
                if (httpRequest.status === 200) {
                    return httpRequest.responseText;
                } else {
                	alert('There was an issue getting the prof list...');
                }
            }
        }
        XHR.send();
    });
}
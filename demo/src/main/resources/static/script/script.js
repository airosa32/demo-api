// Vai fazer conexão do nosso FRONT com a nossa API

const url = "http://localhost:8080/task/user/1";

function hideLoader() {
    document.getElementById("loading").style.display = "none";
}

function show(tasks) {
    // Cria o cabeçalho
    let tab = `<thread>
            <th scope="col">#</th>
            <th scope="col">Description</th>
            <th scope="col">Username</th>
            <th scope="col">User Id</th>
            </thread>`;

    for (let task of tasks) {
        // Itera sobre o cabeçalho e add as linha abaixo dele,
        // com os valores das variaveis task vinda do JAVA.
        tab += `
            <tr>
                <td scope="row">${task.id}</t>
                <td>${task.description}</t>
                <td>${task.user.username}</t>
                <td>${task.user.id}</t>
            </tr>
        `;
    }

    // Vai sobreescrever o dado na tag que tem esse ID, com
    // a tabela criada aqui.
    document.getElementById("tasks").innerHTML = tab;
}

// Essa função é a PRINCIPAL que acessa a API.
// ASYNC -> diz para carregar depois que a pag carregar,
// não é de forma instantanea, não vai ser uma coisa atraz
// da outra, ela pode acontecer a qualquer momento da pag, 
// ela vai ter um tempo  para se comunucar com a API.
async function getAPI(url) {
    // await -> so pode ser usado dentro de asyns
    // e diz para esperar receber todos os dados primeiro
    // antes de fazer qualquer outra coisa.

    // Response -> vai receber os dados da nossa API, vinda
    // do url.
    const response = await fetch(url, {method: "GET"});

    // Transforma os dados em estrutura de JSON.
    var data = await response.json();
    // <- mostra no console do BROWSER os dados
    console.log(data); 

    // Se o response recebeu os dados da nossa API, entao
    // tira o loading.
    if (response) {
        hideLoader();
    }    
    show(data);
}

// URL -> esta definida la em cima do codigo -> localhost...
getAPI(url);

const resultOut = document.getElementById("result-out");
const usersOut = document.getElementById("users-out");
const classesOut = document.getElementById("classes-out");
const coursOut = document.getElementById("cours-out");
const BASE_PATH = window.location.pathname.replace(/\/ui\/?$/, "");

async function api(url, options = {}) {
    const response = await fetch(url, {
        headers: {"Content-Type": "application/json"},
        ...options
    });
    if (!response.ok) {
        const txt = await response.text();
        throw new Error(`${response.status} ${response.statusText}: ${txt}`);
    }
    if (response.status === 204) {
        return null;
    }
    const contentType = response.headers.get("content-type") || "";
    return contentType.includes("application/json") ? response.json() : response.text();
}

function print(node, value) {
    node.textContent = typeof value === "string" ? value : JSON.stringify(value, null, 2);
}

async function refreshAll() {
    const [users, classes, cours] = await Promise.all([
        api(`${BASE_PATH}/api/utilisateurs`),
        api(`${BASE_PATH}/api/classes`),
        api(`${BASE_PATH}/api/cours-classrooms`)
    ]);
    print(usersOut, users);
    print(classesOut, classes);
    print(coursOut, cours);
}

function formDataToObject(form) {
    return Object.fromEntries(new FormData(form).entries());
}

document.getElementById("refresh-all-btn").addEventListener("click", async () => {
    try {
        await refreshAll();
        print(resultOut, "Tableaux rafraîchis.");
    } catch (e) {
        print(resultOut, e.message);
    }
});

document.getElementById("classe-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    const payload = formDataToObject(e.target);
    try {
        const created = await api(`${BASE_PATH}/api/classes`, {method: "POST", body: JSON.stringify(payload)});
        print(resultOut, created);
        e.target.reset();
        await refreshAll();
    } catch (err) {
        print(resultOut, err.message);
    }
});

document.getElementById("user-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    const payload = formDataToObject(e.target);
    try {
        const created = await api(`${BASE_PATH}/api/utilisateurs`, {method: "POST", body: JSON.stringify(payload)});
        print(resultOut, created);
        e.target.reset();
        await refreshAll();
    } catch (err) {
        print(resultOut, err.message);
    }
});

document.getElementById("cours-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    const payload = formDataToObject(e.target);
    const codeClasse = payload.codeClasse;
    delete payload.codeClasse;
    payload.nbHeures = Number(payload.nbHeures);
    payload.archive = false;
    try {
        const created = await api(`${BASE_PATH}/api/cours-classrooms/${codeClasse}`, {method: "POST", body: JSON.stringify(payload)});
        print(resultOut, created);
        e.target.reset();
        await refreshAll();
    } catch (err) {
        print(resultOut, err.message);
    }
});

document.getElementById("affect-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    const payload = formDataToObject(e.target);
    try {
        await api(`${BASE_PATH}/api/utilisateurs/${payload.idUtilisateur}/classes/${payload.codeClasse}`, {method: "PUT"});
        print(resultOut, "Affectation réussie.");
        e.target.reset();
        await refreshAll();
    } catch (err) {
        print(resultOut, err.message);
    }
});

document.getElementById("desaffect-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    const payload = formDataToObject(e.target);
    try {
        await api(`${BASE_PATH}/api/cours-classrooms/${payload.idCours}/desaffecter`, {method: "PUT"});
        print(resultOut, "Cours désaffecté.");
        e.target.reset();
        await refreshAll();
    } catch (err) {
        print(resultOut, err.message);
    }
});

document.getElementById("count-level-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    const payload = formDataToObject(e.target);
    try {
        const count = await api(`${BASE_PATH}/api/utilisateurs/niveau/${payload.niveau}/count`);
        print(resultOut, `Nombre d'utilisateurs (${payload.niveau}) = ${count}`);
    } catch (err) {
        print(resultOut, err.message);
    }
});

document.getElementById("hours-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    const payload = formDataToObject(e.target);
    try {
        const total = await api(`${BASE_PATH}/api/cours-classrooms/heures?sp=${payload.specialite}&nv=${payload.niveau}`);
        print(resultOut, `Nb heures ${payload.specialite}/${payload.niveau} = ${total}`);
    } catch (err) {
        print(resultOut, err.message);
    }
});

document.getElementById("archive-btn").addEventListener("click", async () => {
    try {
        await api(`${BASE_PATH}/api/cours-classrooms/archiver`, {method: "POST"});
        print(resultOut, "Tous les cours sont archivés.");
        await refreshAll();
    } catch (err) {
        print(resultOut, err.message);
    }
});

document.getElementById("seed-btn").addEventListener("click", async () => {
    try {
        const amna = await api(`${BASE_PATH}/api/utilisateurs`, {
            method: "POST",
            body: JSON.stringify({prenom: "Amna", nom: "Ammar", password: "etudiant"})
        });
        const ahmed = await api(`${BASE_PATH}/api/utilisateurs`, {
            method: "POST",
            body: JSON.stringify({prenom: "Ahmed", nom: "Slama", password: "admin"})
        });

        const c4ag1 = await api(`${BASE_PATH}/api/classes`, {
            method: "POST",
            body: JSON.stringify({titre: "4AG1", niveau: "QUATRIEME"})
        });
        const c5em1 = await api(`${BASE_PATH}/api/classes`, {
            method: "POST",
            body: JSON.stringify({titre: "5EM1", niveau: "CINQUIEME"})
        });

        await api(`${BASE_PATH}/api/cours-classrooms/${c4ag1.codeClasse}`, {
            method: "POST",
            body: JSON.stringify({specialite: "INFORMATIQUE", nom: "Programmation C", nbHeures: 42, archive: false})
        });
        await api(`${BASE_PATH}/api/cours-classrooms/${c4ag1.codeClasse}`, {
            method: "POST",
            body: JSON.stringify({specialite: "AGRICULTURE", nom: "Plantes", nbHeures: 25, archive: false})
        });
        await api(`${BASE_PATH}/api/cours-classrooms/${c4ag1.codeClasse}`, {
            method: "POST",
            body: JSON.stringify({specialite: "AGRICULTURE", nom: "Sciences Naturelles", nbHeures: 40, archive: false})
        });

        await api(`${BASE_PATH}/api/utilisateurs/${amna.idUtilisateur}/classes/${c4ag1.codeClasse}`, {method: "PUT"});
        await api(`${BASE_PATH}/api/utilisateurs/${ahmed.idUtilisateur}/classes/${c5em1.codeClasse}`, {method: "PUT"});
        print(resultOut, "Dataset énoncé injecté.");
        await refreshAll();
    } catch (err) {
        print(resultOut, err.message);
    }
});

refreshAll().catch(err => print(resultOut, err.message));

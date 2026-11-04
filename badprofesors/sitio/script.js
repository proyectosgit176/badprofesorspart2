// ---------- 1. Formularios de varias páginas ----------
// Los formularios con data-siguiente guardan sus datos y pasan a la página siguiente.
for (const formulario of document.querySelectorAll('form[data-siguiente]')) {
    formulario.addEventListener('submit', function (evento) {
        evento.preventDefault(); // evita que la página se recargue

        // Solo la encuesta guarda sus respuestas (en el navegador, con localStorage)
        if (formulario.dataset.guardar === 'si') {
            for (const campo of formulario.elements) {
                if (!campo.name || campo.type === 'checkbox') continue;
                if (campo.type === 'radio' && !campo.checked) continue;
                localStorage.setItem(campo.name, campo.value);
            }
            const marcadas = formulario.querySelectorAll('input[name="mejoras"]:checked');
            if (formulario.querySelector('input[name="mejoras"]')) {
                localStorage.setItem('mejoras', marcadas.length);
            }
        }

        if (formulario.dataset.mensaje) alert(formulario.dataset.mensaje);
        window.location.href = formulario.dataset.siguiente;
    });
}

// ---------- 2. Página de resultado ----------
if (document.getElementById('veredicto')) {
    const profesor = localStorage.getItem('profesor') || 'evaluado';
    const curso = localStorage.getItem('grado') || '';
    const comunicacion = localStorage.getItem('comunicacion') || '';
    const comentarios = (localStorage.getItem('comentarios') || '').toLowerCase();
    const mejoras = Number(localStorage.getItem('mejoras')) || 0;

    // Puntaje de 0 a 100 (más puntaje = peor)
    let puntaje = 0;
    if (comunicacion === 'buena') puntaje = 20;
    if (comunicacion === 'regular') puntaje = 50;
    if (comunicacion === 'mala') puntaje = 80;

    puntaje = puntaje + mejoras * 8; // cada mejora pedida suma 8

    const palabrasMalas = ['malo', 'pesimo', 'peor', 'horrible', 'nunca', 'no entiendo', 'no explica', 'aburrido'];
    const palabrasBuenas = ['bueno', 'claro', 'fácil', 'entretenido', 'agradable', 'excelente'];
    for (const palabra of palabrasMalas) {
        if (comentarios.includes(palabra)) puntaje = puntaje + 10;
    }
    for (const palabra of palabrasBuenas) {
        if (comentarios.includes(palabra)) puntaje = puntaje - 8;
    }
    if (puntaje < 0) puntaje = 0;
    if (puntaje > 100) puntaje = 100;

    // Porcentajes de las tres barras
    const malo = puntaje;
    const regular = 100 - Math.abs(puntaje - 50) * 2;
    const bueno = 100 - puntaje;

    // Mensaje final
    let mensaje = '';
    if (puntaje >= 75) {
        mensaje = 'Muy malo. El profesor ' + profesor + ' necesita mejora urgente.';
    } else if (puntaje >= 50) {
        mensaje = 'Malo. El profesor ' + profesor + ' muestra problemas importantes.';
    } else if (puntaje >= 30) {
        mensaje = 'Regular. Hay áreas a mejorar para que la experiencia sea más positiva.';
    } else {
        mensaje = 'Aceptable. La percepción no es mala, pero siempre hay espacio para mejorar.';
    }

    // Mostramos todo en la página
    document.getElementById('veredicto').textContent = mensaje;
    document.getElementById('detalle').textContent = 'Docente: ' + profesor + (curso ? ' · ' + curso : '');
    document.getElementById('barraMalo').style.width = malo + '%';
    document.getElementById('barraRegular').style.width = regular + '%';
    document.getElementById('barraBueno').style.width = bueno + '%';
    document.getElementById('textoMalo').textContent = malo + '%';
    document.getElementById('textoRegular').textContent = regular + '%';
    document.getElementById('textoBueno').textContent = bueno + '%';
}

// ---------- 3. Buscador de docentes ----------
const buscador = document.getElementById('buscador');
if (buscador) {
    buscador.addEventListener('input', function () {
        const texto = buscador.value.toLowerCase();
        for (const tarjeta of document.querySelectorAll('.docente')) {
            const coincide = tarjeta.textContent.toLowerCase().includes(texto);
            tarjeta.style.display = coincide ? '' : 'none';
        }
    });
}

// ---------- 4. Crear cuenta ----------
// Guarda al usuario en el navegador (localStorage). La contraseña NO se guarda a propósito:
// guardarla sin cifrar no es seguro, y cifrarla bien necesita un servidor.
const formularioCuenta = document.getElementById('formulario-cuenta');
if (formularioCuenta) {
    formularioCuenta.addEventListener('submit', function (evento) {
        evento.preventDefault();

        const usuario = {
            nombre: document.getElementById('nombre').value.trim(),
            email: document.getElementById('email').value.trim().toLowerCase(),
            curso: document.getElementById('curso').value
        };

        // Cada usuario se guarda con la clave "usuario_" + su email
        if (localStorage.getItem('usuario_' + usuario.email)) {
            alert('Ya existe una cuenta con ese email.');
            return;
        }
        localStorage.setItem('usuario_' + usuario.email, JSON.stringify(usuario));
        localStorage.setItem('usuarioActual', usuario.email);
        window.location.href = 'encuesta-1.html';
    });
}

// ---------- 5. Ingresar ----------
// Solo comprueba que el email tenga una cuenta creada
const formularioIngreso = document.getElementById('formulario-ingreso');
if (formularioIngreso) {
    formularioIngreso.addEventListener('submit', function (evento) {
        evento.preventDefault();

        const email = document.getElementById('email').value.trim().toLowerCase();
        if (!localStorage.getItem('usuario_' + email)) {
            alert('No encontramos una cuenta con ese email. Creá una primero.');
            return;
        }
        localStorage.setItem('usuarioActual', email);
        window.location.href = 'mis-encuestas.html';
    });
}

// ---------- 6. Mi cuenta: mostrar los datos guardados ----------
const formularioMiCuenta = document.getElementById('formulario-micuenta');
const emailActual = localStorage.getItem('usuarioActual');
if (formularioMiCuenta && emailActual) {
    const guardado = JSON.parse(localStorage.getItem('usuario_' + emailActual));
    if (guardado) {
        document.getElementById('nombre').value = guardado.nombre;
        document.getElementById('email').value = guardado.email;
        document.getElementById('curso').value = guardado.curso;
    }
}

package com.daw.club.model;

import jakarta.security.enterprise.CallerPrincipal;

/** Custom Principal for encapsulating authenticated App domain users info
 * @implNote Access from views, e.g  #{request.userPrincipal.usuario.nombre}
 * @implNote Access from classes:
 *      \@Inject
 *      SecurityContext sc;
 *      ...
 *      sc.getCallerPrincipal().getUsuario.getNombre();
 *
 */
public class ClubPrincipal extends CallerPrincipal {
    private final Cliente usuario; //App domain user info

    public ClubPrincipal(Cliente usuario) {
        super(usuario.getDni()); //Init CallerPrincipal name from app users
        this.usuario = usuario;
    }
    public Cliente getUsuario() {
        return usuario;
    }
}

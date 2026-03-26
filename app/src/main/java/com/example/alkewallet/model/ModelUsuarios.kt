package com.example.alkewallet.model
import com.example.alkewallet.R

 object ModelUsuarios {
     val usuarios = mutableListOf<Usuario>()

fun inicializar() {

    if (usuarios.isNotEmpty()) return

    usuarios.add(
        Usuario(
            1,
            "Yara Kahalil",
            "yara.kahali@gmai.com",
            1000.0,
            R.drawable.profile_picture2
        )
    )

    usuarios.add(
        Usuario(
            2,
            "Sandra Sandres",
            "sandra.sandres@gmail.com",
            100000.0,
            R.drawable.profile_picture3
        )
    )
    usuarios.add(
        Usuario(
            3,
            "Laucha Consarna",
            "laucha.consarna@gmail.com",
            1000.0,
            R.drawable.profile_picture4
        )
    )
    usuarios.add(
        Usuario(
            4,
            "Jimena Jin",
            "jimena.jin@gmail.com",
            1000.0,
            R.drawable.profile_picture5
        )
    )
    usuarios.add(
        Usuario(
            5,
            "Chapulin Colorado",
            "chapulin.colorado@gmail.com",
            1000.0,
            R.drawable.profile_picture6
        )
    )

}
}
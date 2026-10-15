//  Configuración general del proyecto, los pluggins cambian o amplian la forma en
//  la que se contruye la app aqui solo se declaran su aplicación se realiza en el
//  modulo app donde está la aplicación
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.ksp) apply false
}
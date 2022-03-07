package today.sleek.base.modules

import org.lwjgl.input.Keyboard

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ModuleData(
    val name: String,
    val category: ModuleCategory,
    val description: String,
    val bind: Int = Keyboard.KEY_NONE
)
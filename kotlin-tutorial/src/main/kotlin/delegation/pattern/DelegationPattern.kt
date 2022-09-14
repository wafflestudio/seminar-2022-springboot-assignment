package delegation.pattern

interface SoundBehavior {
    fun makeSound()
}

class ScreamBehavior(val n: String) : SoundBehavior {
    override fun makeSound() = println("${n.uppercase()}")
}

class RockAndRollBehavior(val n: String) : SoundBehavior {
    override fun makeSound() = println("I'm The King of Rock 'N' Roll: $n")
}

// Delegation(위임) 패턴을 편하게 쓸 수 있다
// TomAraya 클래스는 SoundBehavior를 구현한 클래스
// makeSound()함수의 구현은 by 절 뒤의 ScreamBehavior(n) 객체에게 위임함
class TomAraya(n: String) : SoundBehavior by ScreamBehavior(n)

class ElvisPresley(n: String) : SoundBehavior by RockAndRollBehavior(n)

fun main() {
    val tomAraya = TomAraya("Thrash Metal")
    tomAraya.makeSound()
    val elvisPresley = ElvisPresley("Dancin' to the Jailhouse Rock")
    elvisPresley.makeSound()
}
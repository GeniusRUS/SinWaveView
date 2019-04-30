package com.genius.waveexample

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.appcompat.app.AppCompatActivity
import android.view.View

/**
 * Суть данного интерфейса в том, что только те, кто явно его реализует, могут изменять навигацию
 * фрагментов на экране. Основные методы реализованы через методы-расширения, которые будут доступны
 * только в соответствующих классах, что делает зависимость навигации явной и не требует еще методов
 * в классе Utils
 *
 * Так как реализации данных методов уже указаны, то не требуется их переопределять в реализующих этот
 * интерфейс, классах. Таким образом, реализуя этот интерфейс, классу гарантированно добавляется функционал
 * этого интерфейса
 */
@Suppress("UNUSED")
interface FragmentNavigation {

    /**
     * Метод-расширение для открытия переданного фрагмента на экране активности
     *
     * Данный метод получает по переданному тэгу [tag] фрагмент из [androidx.fragment.app.FragmentManager]
     * Если найденный фрагмент != null, то на него делается [FragmentTransaction.attach],
     * который заново присоединяет его к контейнеру в активности.
     * В противном случае инстанс фрагмента, переданный в [fragmentInstance] добавляется в [androidx.fragment.app.FragmentManager]
     *
     * Так как параметр [tag] nullable, если в него не передавать значение, то каждый раз будет создаваться
     * новый инстанс фрагмента [fragmentInstance] и добавляться в [androidx.fragment.app.FragmentManager],
     * что не очень хорошо с точки зрения пользовательского опыта и оптимизации работы приложения
     *
     * Если ранее был установлен [androidx.fragment.app.FragmentManager.getPrimaryNavigationFragment],
     * то он находится и на него делается [FragmentTransaction.detach].
     * С помощью этого механизма можно всегда получить верхний активный фрагмент с помощью
     * вызова [androidx.fragment.app.FragmentManager.getPrimaryNavigationFragment]
     *
     * Если задан флаг [isAddToBackStack], то применяется метод [FragmentTransaction.addToBackStack],
     * добавляющий [FragmentTransaction] в стак возврата.
     * По-умолчанию данный флаг стоит в позиции [java.lang.Boolean.FALSE]
     *
     * [FragmentTransaction] задается эффект перехода [FragmentTransaction.TRANSIT_FRAGMENT_FADE],
     * задается флаг оптимизации [FragmentTransaction.setReorderingAllowed] и транзакция добавляется
     * в очередь выполнения [androidx.fragment.app.FragmentManager]
     *
     * Если нужно анимировать переходы из фрагмента во фрагмент, то можно передать инстансы [View] с идентификатором,
     * по которому в открываемом фрагменте будут найдены конечные точки анимации переданных [View].
     * По-умолчанию этот параметр null
     *
     * @param containerId - идентификатор контейнера, в который будет помещён фрагмент
     * @param fragmentInstance - инстанс фрагмента для создания его, если ранее создан не был
     * @param tag - метка фрагмента, по которой можно будет найти фрагмент среди уже созданных
     * @param isAddToBackStack - флаг, говорящий о том, добавлять в backStack [fragmentInstance] или нет
     * @param sharedElements - элементы для анимации перехода из фрагмента во фрагмент
     */
    fun AppCompatActivity.attachFragment(@IdRes containerId: Int, fragmentInstance: Fragment, tag: String?, isAddToBackStack: Boolean = false, sharedElements: Map<String, View>? = null) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        var fragment = supportFragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            fragment = fragmentInstance
            fragmentTransaction.add(containerId, fragment, tag)
        } else {
            fragmentTransaction.attach(fragment)
        }

        val curFrag = supportFragmentManager.primaryNavigationFragment
        if (fragment != curFrag) {
            if (curFrag != null) {
                fragmentTransaction.detach(curFrag)
            }

            if (isAddToBackStack) {
                fragmentTransaction.addToBackStack(tag)
            }

            if (sharedElements != null) {
                for (sharedItem in sharedElements) {
                    fragmentTransaction.addSharedElement(sharedItem.value, sharedItem.key)
                }
            } else {
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            }

            fragmentTransaction
                .setPrimaryNavigationFragment(fragment)
                .setReorderingAllowed(true)
                .commit()
        }
    }

    /**
     * Метод-расширение для открытия переданного фрагмента на экране из другого фрагмента
     *
     * Метод [Fragment.requireActivity] проверяет, что данный фрагмент проассоциирован с [android.app.Activity]
     * и если нет, то выбрасывается стандартное исключение в этом методе [java.lang.IllegalStateException]
     *
     * Проверяется, что [android.app.Activity], к которой прикреплен фрагмент, на который вызывают
     * эту функцию, является [AppCompatActivity], и если да, то запускается метод [attachFragment].
     * Если же проверка не проходит, то происходит возврат из функции
     *
     * Сигнатура метода полностью совпадает с [attachFragment]
     */
    fun Fragment.attachFragment(@IdRes containerId: Int, fragmentInstance: Fragment, tag: String?, isAddToBackStack: Boolean = false, sharedElements: Map<String, View>? = null) {
        if (requireActivity() !is AppCompatActivity) return

        (requireActivity() as AppCompatActivity).attachFragment(containerId, fragmentInstance, tag, isAddToBackStack, sharedElements)
    }
}
package fr.eilco.flycompare

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import fr.eilco.flycompare.utils.Helpers
import fr.eilco.flycompare.utils.Keys

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();
        val isFirstAppOpen = Helpers.loadFromLocal(this.applicationContext, Keys.APP_FIRST_OPEN)

        if (isFirstAppOpen.isEmpty()) {
            setContentView(R.layout.splash_screen)

            // Navigation to Home activity
            val goToNextPage = findViewById<TextView>(R.id.splash_button)

            goToNextPage.setOnClickListener {
                Helpers.storeLocally(
                    this.applicationContext,
                    Keys.APP_FIRST_OPEN,
                    Keys.APP_FIRST_OPEN
                )
                goToHomeActivity()
            }
        } else {
            goToHomeActivity()
        }

    }

    private fun goToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}
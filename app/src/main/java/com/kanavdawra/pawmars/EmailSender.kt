package com.kanavdawra.pawmars

import android.app.ProgressDialog
import android.os.AsyncTask
import java.io.File
import java.io.UnsupportedEncodingException
import java.util.*
import javax.mail.*
import javax.mail.internet.*
import android.content.Intent.getIntent
import android.content.Intent
import android.os.Bundle
import needle.Needle


class EmailSender {
    var jobNo: String? = null
    var teamNo: String? = null
    private var username = "mail@gmail.com"
    private var password = "123456"
    private var emailid = "mail2@outlook.com"
    private var subject = "Photo"
    private var message = "Hello"
    private var multipart = MimeMultipart()
    private var messageBodyPart = MimeBodyPart()
    var mediaFile: File? = null



     fun sendMail(email: String, subject: String, messageBody: String,username:String,password:String) {
        this.username=username
        this.password=password
        val session = createSessionObject()

        try {
            val message = createMessage(email, subject, messageBody, session)
            Needle.onBackgroundThread().withThreadPoolSize(10).execute{
                try {
                    Transport.send(message)
                } catch (e: MessagingException) {
                    e.printStackTrace()
                }
            }
        } catch (e: AddressException) {
            e.printStackTrace()
        } catch (e: MessagingException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

    }

    private fun createSessionObject(): Session {
        val properties = Properties()
        properties.put("mail.smtp.auth", "true")
        properties.put("mail.smtp.starttls.enable", "true")
        properties.put("mail.smtp.host", "smtp.gmail.com")
        properties.put("mail.smtp.port", "587")

        return Session.getInstance(properties, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })
    }

    @Throws(MessagingException::class, UnsupportedEncodingException::class)
    private fun createMessage(email: String, subject: String, messageBody: String, session: Session): Message {
        val message = MimeMessage(session)
        message.setFrom(InternetAddress("mail2@outlook.com", "Naveed Qureshi"))
        message.addRecipient(Message.RecipientType.TO, InternetAddress(email, email))
        message.setSubject(subject)
        message.setText(messageBody)
        return message
    }

}
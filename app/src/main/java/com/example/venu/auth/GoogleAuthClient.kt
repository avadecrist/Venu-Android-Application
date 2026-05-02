package com.example.venu.auth

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.example.venu.R
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CancellationException

class GoogleAuthClient(
    private val context: Context
) {
    private val credentialManager = CredentialManager.create(context)

    suspend fun signIn(): Result<AppUser> {
        return try {

            val googleOption = GetSignInWithGoogleOption.Builder(
                serverClientId = context.getString(R.string.google_web_client_id)
            ).build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleOption)
                .build()

            val response = credentialManager.getCredential(
                context = context,
                request = request
            )

            val credential = response.credential

            if (credential !is CustomCredential) {
                return Result.failure(
                    IllegalStateException("Unsupported credential class: ${credential::class.qualifiedName}")
                )
            }

            val isGoogleCredential =
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL ||
                        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_SIWG_CREDENTIAL

            if (!isGoogleCredential) {
                return Result.failure(
                    IllegalStateException("Unsupported credential type: ${credential.type}")
                )
            }

            val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)

            Result.success(
                AppUser(
                    email = googleCredential.id,
                    displayName = googleCredential.displayName,
                    profilePictureUri = googleCredential.profilePictureUri?.toString()
                )
            )
        } catch (e: GetCredentialCancellationException) {
            Result.failure(e)
        } catch (e: GoogleIdTokenParsingException) {
            Result.failure(e)
        } catch (e: GetCredentialException) {
            Result.failure(e)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signOut() {
        credentialManager.clearCredentialState(
            ClearCredentialStateRequest()
        )
    }
}
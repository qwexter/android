package com.x8bit.bitwarden.data.vault.repository

import android.net.Uri
import com.bitwarden.bitwarden.ExportFormat
import com.bitwarden.bitwarden.SendType
import com.bitwarden.bitwarden.SendView
import com.bitwarden.core.DateTime
import com.bitwarden.crypto.Kdf
import com.bitwarden.vault.CipherView
import com.bitwarden.vault.CollectionView
import com.bitwarden.vault.FolderView
import com.x8bit.bitwarden.data.platform.repository.model.DataState
import com.x8bit.bitwarden.data.vault.manager.CipherManager
import com.x8bit.bitwarden.data.vault.manager.VaultLockManager
import com.x8bit.bitwarden.data.vault.manager.model.VerificationCodeItem
import com.x8bit.bitwarden.data.vault.repository.model.CreateFolderResult
import com.x8bit.bitwarden.data.vault.repository.model.CreateSendResult
import com.x8bit.bitwarden.data.vault.repository.model.DeleteFolderResult
import com.x8bit.bitwarden.data.vault.repository.model.DeleteSendResult
import com.x8bit.bitwarden.data.vault.repository.model.DomainsData
import com.x8bit.bitwarden.data.vault.repository.model.ExportVaultDataResult
import com.x8bit.bitwarden.data.vault.repository.model.GenerateTotpResult
import com.x8bit.bitwarden.data.vault.repository.model.RemovePasswordSendResult
import com.x8bit.bitwarden.data.vault.repository.model.SendData
import com.x8bit.bitwarden.data.vault.repository.model.TotpCodeResult
import com.x8bit.bitwarden.data.vault.repository.model.UpdateFolderResult
import com.x8bit.bitwarden.data.vault.repository.model.UpdateSendResult
import com.x8bit.bitwarden.data.vault.repository.model.VaultData
import com.x8bit.bitwarden.data.vault.repository.model.VaultUnlockResult
import com.x8bit.bitwarden.ui.vault.feature.vault.model.VaultFilterType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Responsible for managing vault data inside the network layer.
 */
@Suppress("TooManyFunctions")
interface VaultRepository : CipherManager, VaultLockManager {

    /**
     * The [VaultFilterType] for the current user.
     *
     * Note that this does not affect the data provided by the repository and can be used by
     * the UI for consistent filtering across screens.
     */
    var vaultFilterType: VaultFilterType

    /**
     * Flow that represents the current vault data.
     *
     * Note that the [StateFlow.value] will return the last known value but the [StateFlow] itself
     * must be collected in order to trigger state changes.
     */
    val vaultDataStateFlow: StateFlow<DataState<VaultData>>

    /**
     * Flow that represents all ciphers for the active user.
     *
     * Note that the [StateFlow.value] will return the last known value but the [StateFlow] itself
     * must be collected in order to trigger state changes.
     */
    val ciphersStateFlow: StateFlow<DataState<List<CipherView>>>

    /**
     * Flow that represents all collections for the active user.
     *
     * Note that the [StateFlow.value] will return the last known value but the [StateFlow] itself
     * must be collected in order to trigger state changes.
     */
    val collectionsStateFlow: StateFlow<DataState<List<CollectionView>>>

    /**
     * Flow that represents all domains for the active user.
     *
     * Note that the [StateFlow.value] will return the last known value but the [StateFlow] itself
     * must be collected in order to trigger state changes.
     */
    val domainsStateFlow: StateFlow<DataState<DomainsData>>

    /**
     * Flow that represents all folders for the active user.
     *
     * Note that the [StateFlow.value] will return the last known value but the [StateFlow] itself
     * must be collected in order to trigger state changes.
     */
    val foldersStateFlow: StateFlow<DataState<List<FolderView>>>

    /**
     * Flow that represents the current send data.
     */
    val sendDataStateFlow: StateFlow<DataState<SendData>>

    /**
     * Flow that represents the totp code.
     */
    val totpCodeFlow: Flow<TotpCodeResult>

    /**
     * Completely remove any persisted data from the vault.
     */
    fun deleteVaultData(userId: String)

    /**
     * Sync the vault data for the current user.
     *
     * Unlike [syncIfNecessary], this will always perform the requested sync and should only be
     * utilized in cases where the user specifically requested the action.
     */
    fun sync()

    /**
     * Checks if conditions have been met to perform a sync request and, if so, syncs the vault
     * data for the current user.
     */
    fun syncIfNecessary()

    /**
     * Flow that represents the data for a specific vault item as found by ID. This may emit `null`
     * if the item cannot be found.
     */
    fun getVaultItemStateFlow(itemId: String): StateFlow<DataState<CipherView?>>

    /**
     * Flow that represents the data for a specific vault folder as found by ID. This may emit
     * `null` if the folder cannot be found.
     */
    fun getVaultFolderStateFlow(folderId: String): StateFlow<DataState<FolderView?>>

    /**
     * Flow that represents the data for a specific send as found by ID. This may emit `null` if
     * the send cannot be found.
     */
    fun getSendStateFlow(sendId: String): StateFlow<DataState<SendView?>>

    /**
     * Flow that represents the data for a single verification code item.
     * This may emit null if any issues arise during code generation.
     */
    fun getAuthCodeFlow(cipherId: String): StateFlow<DataState<VerificationCodeItem?>>

    /**
     * Flow that represents the data for the TOTP verification codes for ciphers items.
     * This may emit an empty list if any issues arise during code generation.
     */
    fun getAuthCodesFlow(): StateFlow<DataState<List<VerificationCodeItem>>>

    /**
     * Emits the totp code result flow to listeners.
     */
    fun emitTotpCodeResult(totpCodeResult: TotpCodeResult)

    /**
     * Attempt to unlock the vault using the stored biometric key for the currently active user.
     */
    suspend fun unlockVaultWithBiometrics(): VaultUnlockResult

    /**
     * Attempt to unlock the vault with the given [masterPassword] and for the currently active
     * user.
     */
    suspend fun unlockVaultWithMasterPassword(
        masterPassword: String,
    ): VaultUnlockResult

    /**
     * Attempt to unlock the vault with the given [pin] for the currently active user.
     */
    suspend fun unlockVaultWithPin(
        pin: String,
    ): VaultUnlockResult

    /**
     * Attempt to unlock the vault with the specified user information.
     *
     * Note that when [organizationKeys] is absent, no attempt will be made to unlock the vault
     * for organization data.
     */
    @Suppress("LongParameterList")
    suspend fun unlockVault(
        userId: String,
        masterPassword: String,
        email: String,
        kdf: Kdf,
        userKey: String,
        privateKey: String,
        organizationKeys: Map<String, String>?,
    ): VaultUnlockResult

    /**
     * Attempt to create a send. The [fileUri] _must_ be present when the given [SendView] has a
     * [SendView.type] of [SendType.FILE].
     */
    suspend fun createSend(sendView: SendView, fileUri: Uri?): CreateSendResult

    /**
     * Attempt to update a send.
     */
    suspend fun updateSend(
        sendId: String,
        sendView: SendView,
    ): UpdateSendResult

    /**
     * Attempt to remove the password from a send.
     */
    suspend fun removePasswordSend(sendId: String): RemovePasswordSendResult

    /**
     * Attempt to get the verification code and the period.
     */
    suspend fun generateTotp(totpCode: String, time: DateTime): GenerateTotpResult

    /**
     * Attempt to delete a send.
     */
    suspend fun deleteSend(sendId: String): DeleteSendResult

    /**
     * Attempt to create a folder.
     */
    suspend fun createFolder(folderView: FolderView): CreateFolderResult

    /**
     * Attempt to delete a folder.
     */
    suspend fun deleteFolder(folderId: String): DeleteFolderResult

    /**
     * Attempt to update a folder.
     */
    suspend fun updateFolder(folderId: String, folderView: FolderView): UpdateFolderResult

    /**
     * Attempt to get the user's vault data for export.
     */
    suspend fun exportVaultDataToString(format: ExportFormat): ExportVaultDataResult

    /**
     * Checks if the user should see the unassigned items message.
     */
    suspend fun shouldShowUnassignedItemsInfo(): Boolean

    /**
     * Sets the value indicating that the user has or has not acknowledged that their organization
     * has unassigned items.
     */
    fun acknowledgeUnassignedItemsInfo(hasAcknowledged: Boolean)
}

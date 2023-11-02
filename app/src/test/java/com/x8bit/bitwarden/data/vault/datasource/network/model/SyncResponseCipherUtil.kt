package com.x8bit.bitwarden.data.vault.datasource.network.model

import java.time.LocalDateTime

/**
 * Create a mock [SyncResponseJson.Cipher] with a given [number].
 */
fun createMockCipher(number: Int): SyncResponseJson.Cipher =
    SyncResponseJson.Cipher(
        id = "mockId-$number",
        organizationId = "mockOrganizationId-$number",
        folderId = "mockFolderId-$number",
        collectionIds = listOf("mockCollectionId-$number"),
        name = "mockName-$number",
        notes = "mockNotes-$number",
        type = CipherTypeJson.LOGIN,
        login = createMockLogin(number = number),
        creationDate = LocalDateTime.parse("2023-10-27T12:00:00"),
        deletedDate = LocalDateTime.parse("2023-10-27T12:00:00"),
        revisionDate = LocalDateTime.parse("2023-10-27T12:00:00"),
        attachments = listOf(createMockAttachment(number = number)),
        card = createMockCard(number = number),
        fields = listOf(createMockField(number = number)),
        identity = createMockIdentity(number = number),
        isFavorite = false,
        passwordHistory = listOf(createMockPasswordHistory(number = number)),
        reprompt = CipherRepromptTypeJson.NONE,
        secureNote = createMockSecureNote(),
        shouldEdit = false,
        shouldOrganizationUseTotp = false,
        shouldViewPassword = false,
        key = "mockKey-$number",
    )

/**
 * Create a mock [SyncResponseJson.Cipher.Identity] with a given [number].
 */
fun createMockIdentity(number: Int): SyncResponseJson.Cipher.Identity =
    SyncResponseJson.Cipher.Identity(
        firstName = "mockFirstName-$number",
        middleName = "mockMiddleName-$number",
        lastName = "mockLastName-$number",
        passportNumber = "mockPassportNumber-$number",
        country = "mockCountry-$number",
        address1 = "mockAddress1-$number",
        address2 = "mockAddress2-$number",
        address3 = "mockAddress3-$number",
        city = "mockCity-$number",
        postalCode = "mockPostalCode-$number",
        title = "mockTitle-$number",
        ssn = "mockSsn-$number",
        phone = "mockPhone-$number",
        company = "mockCompany-$number",
        licenseNumber = "mockLicenseNumber-$number",
        state = "mockState-$number",
        email = "mockEmail-$number",
        username = "mockUsername-$number",
    )

/**
 * Create a mock [SyncResponseJson.Cipher.Attachment] with a given [number].
 */
fun createMockAttachment(number: Int): SyncResponseJson.Cipher.Attachment =
    SyncResponseJson.Cipher.Attachment(
        fileName = "mockFileName-$number",
        size = 1,
        sizeName = "mockSizeName-$number",
        id = "mockId-$number",
        url = "mockUrl-$number",
        key = "mockKey-$number",
    )

/**
 * Create a mock [SyncResponseJson.Cipher.Card] with a given [number].
 */
fun createMockCard(number: Int): SyncResponseJson.Cipher.Card =
    SyncResponseJson.Cipher.Card(
        number = "mockNumber-$number",
        expMonth = "mockExpMonth-$number",
        code = "mockCode-$number",
        expirationYear = "mockExpirationYear-$number",
        cardholderName = "mockCardholderName-$number",
        brand = "mockBrand-$number",
    )

/**
 * Create a mock [SyncResponseJson.Cipher.PasswordHistory] with a given [number].
 */
fun createMockPasswordHistory(number: Int): SyncResponseJson.Cipher.PasswordHistory =
    SyncResponseJson.Cipher.PasswordHistory(
        password = "mockPassword-$number",
        lastUsedDate = LocalDateTime.parse("2023-10-27T12:00:00"),
    )

/**
 * Create a mock [SyncResponseJson.Cipher.SecureNote].
 */
fun createMockSecureNote(): SyncResponseJson.Cipher.SecureNote =
    SyncResponseJson.Cipher.SecureNote(
        type = SecureNoteTypeJson.GENERIC,
    )

/**
 * Create a mock [SyncResponseJson.Cipher.Field] with a given [number].
 */
fun createMockField(number: Int): SyncResponseJson.Cipher.Field =
    SyncResponseJson.Cipher.Field(
        linkedIdType = LinkedIdTypeJson.LOGIN_USERNAME,
        name = "mockName-$number",
        type = FieldTypeJson.HIDDEN,
        value = "mockValue-$number",
    )

/**
 * Create a mock [SyncResponseJson.Cipher.Login] with a given [number].
 */
fun createMockLogin(number: Int): SyncResponseJson.Cipher.Login =
    SyncResponseJson.Cipher.Login(
        username = "mockUsername-$number",
        password = "mockPassword-$number",
        passwordRevisionDate = LocalDateTime.parse("2023-10-27T12:00:00"),
        shouldAutofillOnPageLoad = false,
        uri = "mockUri-$number",
        uris = listOf(createMockUri(number = number)),
        totp = "mockTotp-$number",
    )

/**
 * Create a mock [SyncResponseJson.Cipher.Login.Uri] with a given [number].
 */
fun createMockUri(number: Int): SyncResponseJson.Cipher.Login.Uri =
    SyncResponseJson.Cipher.Login.Uri(
        uri = "mockUri-$number",
        uriMatchType = UriMatchTypeJson.HOST,
    )

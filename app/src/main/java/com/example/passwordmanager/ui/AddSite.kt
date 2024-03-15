package com.example.passwordmanager.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.passwordmanager.R
import com.example.passwordmanager.ui.theme.PasswordManagerTheme

@Composable
fun AddSiteScreen(
    viewModel: PassViewModel = viewModel(),
    onCancelButtonClicked: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        OutlinedTextField(
            value = viewModel.siteName,
            onValueChange = { viewModel.setSiteName(it) },
            label = { Text("Site Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(id = R.dimen.padding_medium)),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )

        OutlinedTextField(
            value = viewModel.url,
            onValueChange = { viewModel.setUrl(it) },
            label = { Text("URL") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(id = R.dimen.padding_medium)),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )

        )

        OutlinedTextField(
            value = viewModel.personName,
            onValueChange = { viewModel.setPersonName(it) },
            label = { Text("Login") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(id = R.dimen.padding_medium)),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )

        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.setPassword(it) },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(id = R.dimen.padding_medium)),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))

        Button(
            onClick = {
                if (viewModel.siteName.isNotBlank() &&
                    viewModel.url.isNotBlank() &&
                    viewModel.personName.isNotBlank() &&
                    viewModel.password.isNotBlank()
                ) {
                    viewModel.addSite()
                    onCancelButtonClicked()
                } else {
                    onCancelButtonClicked()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.height))
        ) {
            Text("Add Site")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    PasswordManagerTheme {
        AddSiteScreen()
    }
}
package com.jereksel.libresubstratum.rootlesso.protocol

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id.CLASS

@JsonTypeInfo(use = CLASS,
        include = PROPERTY,
        property = "type")
@JsonSubTypes(
        Type(value = HearthBeat::class, name = "HearthBeat"),
        Type(value = FailedInvocation::class, name = "FailedInvocation"),
        Type(value = OverlaysForTargetRequest::class, name = "OverlaysForTargetRequest"),
        Type(value = OverlaysForTargetResult::class, name = "OverlaysForTargetResult"),
        Type(value = OverlayInfoRequest::class, name = "OverlayInfoRequest"),
        Type(value = OverlayInfoResult::class, name = "OverlayInfoResult"),
        Type(value = Success::class, name = "Success"),
        Type(value = EnableOverlays::class, name = "EnableOverlays"),
        Type(value = InstallPackage::class, name = "InstallPackage")
)
interface Message
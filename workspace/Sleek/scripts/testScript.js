//var script = Java.type("today.sleek.base.scripting.base.ScriptAPI")
var module = script.registerModule("Nig")

module.on('enable', function() {
chat.chat('Script made by Divine')
})

module.on('move', function(event) {
player.setSpeed(event, 1)
    if (mc.thePlayer.onGround) {
        player.setMotionY(0.42)
    }
})
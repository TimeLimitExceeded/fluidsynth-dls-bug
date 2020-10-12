#include <jni.h>
#include <string>
#include <fluidsynth.h>
#include <unistd.h>

extern "C" JNIEXPORT jstring JNICALL Java_com_example_fluidsynthandroidhelloworld_MainActivity_stringFromJNI(JNIEnv* env, jobject, jstring soundfont) {
    // Setup synthesizer
    fluid_settings_t *settings = new_fluid_settings();
    fluid_synth_t *synth = new_fluid_synth(settings);
    fluid_audio_driver_t *adriver = new_fluid_audio_driver(settings, synth);

    // Load sample soundfont
    const char* soundfontPath = env->GetStringUTFChars(soundfont, nullptr);
    int sfont_id = fluid_synth_sfload(synth, soundfontPath, 1);

    std::string result;
    if (sfont_id == FLUID_FAILED) {
        result = "Failed to load soundfont";
    } else {
        // succesfully loaded soundfont...play something
        fluid_synth_noteon(synth, 0, 60, 127); // play middle C
        sleep(1); // sleep for 1 second
        fluid_synth_noteoff(synth, 0, 60); // stop playing middle C

        fluid_synth_noteon(synth, 0, 62, 127);
        sleep(1);
        fluid_synth_noteoff(synth, 0, 62);

        fluid_synth_noteon(synth, 0, 64, 127);
        sleep(1);
        fluid_synth_noteoff(synth, 0, 64);

        result = "success";
    }

    // Clean up
    delete_fluid_audio_driver(adriver);
    delete_fluid_synth(synth);
    delete_fluid_settings(settings);

    return env->NewStringUTF(result.c_str());
}

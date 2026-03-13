# F-Droid Submission Guide

This document explains how to submit Keygen FM to F-Droid.

## Prerequisites

- [ ] App is fully open source (GPL-3.0-or-later) ✅
- [ ] All dependencies are FOSS ✅
- [ ] Repository is public on GitHub/GitLab
- [ ] Code is tagged with version (e.g., `v1.0.1`)
- [ ] Metadata files are created ✅

## Submission Steps

### 1. Push Your Repository

Make sure your code is pushed to a public Git repository (GitHub, GitLab, Codeberg, etc.):

```bash
git add .
git commit -m "Prepare for F-Droid submission"
git tag -a v1.0.1 -m "Release v1.0.1 for F-Droid"
git push origin main --tags
```

### 2. Update Metadata

Edit `metadata/app.kodatek.keygenfm.yml` and replace placeholders:
- `SourceCode: https://github.com/YOURUSERNAME/keygenfm`
- `IssueTracker: https://github.com/YOURUSERNAME/keygenfm/issues`
- `Repo: https://github.com/YOURUSERNAME/keygenfm`

Replace with your actual repository URLs.

### 3. Submit to F-Droid

There are two ways to submit:

#### Option A: Request For Packaging (RFP) - Easiest

1. Go to [F-Droid's Request for Packaging tracker](https://gitlab.com/fdroid/rfp/-/issues)
2. Click "New Issue"
3. Fill in the template:
   ```
   **App Name:** Keygen FM
   **Package ID:** app.kodatek.keygenfm
   **Repository:** https://github.com/YOURUSERNAME/keygenfm
   **License:** GPL-3.0-or-later
   **Description:** 24/7 chiptune and demoscene radio with retro Win95 UI
   ```
4. Wait for F-Droid maintainers to review and add your app

#### Option B: Submit Merge Request - Faster

1. Fork [fdroiddata](https://gitlab.com/fdroid/fdroiddata)
2. Copy `metadata/app.kodatek.keygenfm.yml` to the fork at `metadata/app.kodatek.keygenfm.yml`
3. Commit and create a merge request:
   ```bash
   git clone https://gitlab.com/YOURUSERNAME/fdroiddata
   cd fdroiddata
   cp /path/to/keygenfm/metadata/app.kodatek.keygenfm.yml metadata/
   git add metadata/app.kodatek.keygenfm.yml
   git commit -m "New app: Keygen FM"
   git push origin main
   ```
4. Create merge request to fdroid/fdroiddata on GitLab

### 4. Add Graphics (Optional but Recommended)

F-Droid displays screenshots and icons. Add these to your repo:

```
metadata/en-US/
├── images/
│   ├── icon.png           (512x512px, app icon)
│   ├── featureGraphic.png (1024x500px, banner)
│   └── phoneScreenshots/
│       ├── 1.png          (Screenshots)
│       ├── 2.png
│       └── 3.png
```

You can generate these from your existing app assets in `android/graphics/`.

### 5. Build Test (Optional)

Test that F-Droid can build your app:

```bash
# Install fdroidserver
sudo apt-get install fdroidserver

# Test build
cd /tmp
git clone https://github.com/YOURUSERNAME/keygenfm
cd keygenfm
fdroid build app.kodatek.keygenfm:2
```

## Important Notes

### Reproducible Builds

F-Droid builds apps from source on their servers. The build process:
1. Clones your repo at the specified commit/tag
2. Runs the `prebuild` commands to remove signing configs
3. Builds with `gradle yes` (no Google Play services)
4. Signs with F-Droid's key

### Signing

- F-Droid builds are signed with **F-Droid's key**, not your release keystore
- This means F-Droid builds cannot update Play Store builds (different signature)
- Users who install from F-Droid must uninstall to switch to Play Store

### Update Process

After initial inclusion, F-Droid will auto-detect new releases if:
- `AutoUpdateMode: Version` is set
- `UpdateCheckMode: Tags` is set
- You create git tags like `v1.0.2`, `v1.0.3`, etc.

Just tag new releases and F-Droid will pick them up automatically!

## Troubleshooting

### Build Fails

- Check that JDK 17 is used (not 21+)
- Ensure no proprietary dependencies
- Verify gradle wrapper is committed
- Test local build: `./gradlew assembleRelease`

### Google Fonts Issue

The app uses `androidx.ui.text.google.fonts` which downloads fonts at runtime. This is allowed by F-Droid as long as the app works offline. If reviewers flag this:

1. Create an F-Droid build flavor without Google Fonts
2. Use bundled fonts instead (OFL-licensed fonts from Google Fonts can be bundled)

## Timeline

- **RFP method:** 1-4 weeks for review and inclusion
- **MR method:** 3-7 days if metadata is correct

## Resources

- [F-Droid Docs: Submitting Apps](https://f-droid.org/docs/Submitting_to_F-Droid/)
- [F-Droid Metadata Reference](https://f-droid.org/docs/Build_Metadata_Reference/)
- [fdroiddata repository](https://gitlab.com/fdroid/fdroiddata)
- [F-Droid RFP tracker](https://gitlab.com/fdroid/rfp/-/issues)

# 團隊開發環境設定

這是我們隊伍的 FTC 程式碼。基於 FIRST 官方 SDK **v12.0**（BIOBUZZ 2026-2027 賽季）。

照著下面做，大約 30 分鐘可以讓環境跑起來。**請完整讀完再開始**，有幾個步驟跳過會白做工。

---

## 0. 前置檢查

### 磁碟空間

**至少留 20 GB 可用空間。** 這是最常見的卡關原因 —— 空間不足時 Gradle 不會明講，只會丟出看似無關的錯誤。

先確認：

```bash
df -h ~
```

不夠的話先清理再往下。

### 安裝 Android Studio

從 https://developer.android.com/studio 下載安裝。

第一次啟動會問要不要登入 Google 帳號啟用 Gemini —— **選 Skip for now**，我們用不到。

---

## 1. Clone 專案

```bash
git clone https://github.com/yue1025/Overcoming-2027.git
```

放在純英文、沒有空格的路徑比較保險（中文路徑實測可行，但出問題時比較難查）。

---

## 2. 安裝必要的 SDK 元件 ⚠️

**這步最容易被跳過，而跳過的話 Gradle sync 一定失敗。**

Android Studio 新安裝只會帶最新版的 Android SDK，但 FTC SDK 鎖定在舊版本：

| 元件 | 版本 |
|---|---|
| Android SDK Platform | **API 30**（Android 11） |
| NDK | **21.3.6528147** |

### 安裝方式 A：Android Studio 圖形介面

1. 開啟設定（macOS `⌘,` / Windows `Ctrl+Alt+S`）
2. **Languages & Frameworks → Android SDK**
3. **SDK Platforms** 分頁 → 勾選 **Android 11.0 (R) — API Level 30**
4. **SDK Tools** 分頁 → 右下角勾選 **Show Package Details** → 展開 **NDK (Side by side)** → 勾選 **21.3.6528147**
5. **Apply** → 同意授權條款 → 等它下載（約 1.5 GB）

### 安裝方式 B：命令列

需要先有 `sdkmanager`（macOS 可用 `brew install --cask android-commandlinetools`）：

```bash
sdkmanager "platforms;android-30" "ndk;21.3.6528147"
```

---

## 3. 開啟專案

Android Studio 歡迎畫面選 **Open**（不是 Clone，已經下載好了），選到 clone 下來的資料夾。

開啟後會自動跑 **Gradle sync**，第一次要下載依賴，約 10–20 分鐘，保持連線。

底部 Build 面板出現 sync 成功、左側 Project 面板的模組可以展開，就完成了。

### 驗證

命令列跑一次編譯確認：

```bash
./gradlew :TeamCode:assembleDebug
```

看到 `BUILD SUCCESSFUL` 就代表環境沒問題。

---

## 4. 專案結構 —— 哪裡能動、哪裡不能動

```
Overcoming-2027/
├── TeamCode/                      ← ✅ 我們的程式碼只放這裡
│   └── src/main/java/org/firstinspires/ftc/teamcode/
│
├── FtcRobotController/            ← ❌ 官方模組，不要修改
│   └── .../external/samples/         66 個官方範例，複製出來用
│
├── build.gradle 等                ← ❌ 除非有共識，不要改
└── TEAM_SETUP.md                  ← 這份文件
```

**改到 `FtcRobotController/` 或根目錄的 gradle 檔會讓之後更新官方 SDK 變得很痛苦。** 有需要改再討論。

### 怎麼用官方範例

1. 在 `FtcRobotController/.../samples/` 找到要的範例
2. `⌘C` 複製 → 貼到 `TeamCode` 的 `teamcode` 套件底下 → 取新名字
3. **刪掉 `@Disabled` 那行**（否則 Driver Station 選單上看不到）
4. 改 `@TeleOp(name="...")` 的顯示名稱
5. 改 `hardwareMap.get(...)` 裡的字串，對應實機 configuration 的命名

---

## 5. Git 協作規則

### 每次開始寫之前

```bash
git pull
```

忘記 pull 就動手，是製造衝突最快的方法。

### 開分支做事，不要直接推 master

```bash
git switch -c 你的名字/功能描述
# 例如：git switch -c yue/mecanum-drive
```

寫完之後：

```bash
git add .
git commit -m "說明這次改了什麼"
git push -u origin 你的名字/功能描述
```

然後到 GitHub 開 Pull Request，請另一個人看過再合併。

### 為什麼不直接推 master

比賽前一天 master 壞掉而且不知道是誰推的，是真的會發生的事。分支 + PR 讓每個改動都有紀錄、可以回溯、至少有第二個人看過。

### 不要 commit 的東西

`.gitignore` 已經處理好了，但要知道原因：

- `local.properties` —— 每個人的 Android SDK 路徑不同，commit 會互相覆蓋
- `build/`、`.gradle/` —— 編譯產物，每次都會重新產生
- `.idea/` 大部分內容 —— IDE 個人設定

---

## 6. 常見錯誤

| 症狀 | 原因 |
|---|---|
| Gradle sync 失敗，訊息提到 `android-30` | 第 2 步的 API 30 沒裝 |
| Gradle sync 失敗，訊息提到 `ndk` | 第 2 步的 NDK 21.3.6528147 沒裝 |
| Gradle sync 卡住或莫名失敗 | 磁碟空間不足 |
| Driver Station 選單找不到我的 OpMode | `@Disabled` 忘記刪 |
| 按 INIT 就閃退，說找不到裝置 | `hardwareMap` 的字串跟 configuration 命名不符 |
| 推搖桿前進結果後退 | 搖桿 Y 軸是反的，記得加負號 |
| 按 STOP 沒反應，被強制中止 | 主迴圈條件要用 `opModeIsActive()`，不要用 `while(true)` |

---

## 7. 關於部署

**FTC 不能用 Android 模擬器。** 右上角裝置下拉如果顯示 `Medium Phone API ...` 之類的，那是模擬器，按 Run 不會有任何作用。

正確做法是 USB-C 線接上 **Control Hub**，裝置下拉才會出現實體裝置，選它再按 Run。

沒有 Control Hub 在手邊時，程式碼還是可以寫、可以編譯，只是不能實際跑。

# Control Hub & Driver Hub 連接方式

這份文件說明電腦、Control Hub、Driver Hub 三者怎麼連起來，以及從寫完程式到在 Driver Hub 上跑起來的完整流程。開發環境還沒裝好的話，先看 [TEAM_SETUP.md](TEAM_SETUP.md)。

**請先讀第 0 節**，搞清楚每台設備負責什麼，後面才不會連錯對象。

---

## 0. 三台設備各做什麼

| 設備 | 負責 |
|---|---|
| **電腦（Mac）** | 寫程式、把程式部署到 Control Hub |
| **Control Hub** | 實際執行程式，接馬達、伺服、感測器；同時發出 Wi-Fi |
| **Driver Hub** | 跑 **Driver Station App**：選程式、按 INIT / Start、接遙控器、看 telemetry |

```
電腦 ──(Wi-Fi 或 USB-C)──▶ Control Hub ◀──(Wi-Fi)── Driver Hub ◀── 遙控器
```

重點：

- **程式是部署到 Control Hub，不是 Driver Hub。** 電腦平常不需要連 Driver Hub。
- **Driver Station App 只跑在 Driver Hub 上**（出廠已裝），沒有 Mac 版，電腦不用裝。

---

## 1. Control Hub 開機

Control Hub **本身沒有開關**，通電就自動開機。

接法：**電池 → 主電源開關 → Control Hub 的 XT30 電源孔**（兩個 XT30 孔接哪個都可以）。比賽規定一定要有主電源開關。

開機約 20–30 秒，看燈號：

| 燈號 | 意思 |
|---|---|
| 藍燈 | 開機中 |
| **綠燈** | **好了**，Robot Controller App 已在跑 |
| 黃 / 橘燈閃爍 | 電池電壓太低，去充電 |
| 不亮 | 電池沒電、保險絲斷、線沒插緊 |

**部署程式途中不要斷電。**

---

## 2. Driver Hub 開機並配對 Control Hub

只要做一次，之後開機會自動連。

1. **長按電源鍵**開機（開不了先用 USB-C 充電），會自動進入 Driver Station App
2. 確認 Control Hub 已亮綠燈
3. 右上角 **⋮ → Settings → Pair with Robot Controller**
4. 選 Control Hub 的網路名稱（我們隊目前是 `Team5`，以 Driver Hub 上顯示的為準）
5. 要密碼的話，出廠預設是 `password`
6. 返回主畫面

主畫面上方出現 **網路名稱 + Ping 數字 + 機器人電池電壓**，就是連上了。

### 查 Control Hub 的網路名稱和密碼

- Driver Hub：**⋮ → Program & Manage**
- 或電腦連上 Control Hub 後，在 Robot Controller Console 點 **Manage**（見第 4 節）

---

## 3. 建立 Configuration ⚠️

**沒有 active 的 Configuration，程式一按 INIT 就會說找不到裝置。**

在 Driver Hub 上：

1. **⋮ → Configure Robot → New → Scan**
2. 掃到 Control Hub 後按 **Save**，取個名字
3. 回到列表按 **Activate**

掃描會自動加入 Control Hub 內建的 IMU，名稱是 `imu`。之後接上馬達，要在這裡幫每個 port 取名字，**名字必須跟程式裡 `hardwareMap.get(...)` 的字串一模一樣**（大小寫也算）。

---

## 4. 電腦連上 Control Hub

### ⚠️ Control Hub 的 Wi-Fi 沒有網路

連上 Control Hub 的 Wi-Fi 後，**電腦就不能上網了**（查資料、git push、問 AI 都不行）。要上網得切回一般 Wi-Fi，部署時再切回來。

用 Android Studio 的話，**Gradle 第一次編譯要先在有網路的地方做一次**，不然切到 Control Hub 後會因為下載不到依賴而失敗。

### 方法 A：Wi-Fi

1. 電腦 Wi-Fi 連上 Control Hub 的網路
2. 瀏覽器打開 **http://192.168.43.1:8080**，就是 Robot Controller Console（Blocks / OnBotJava / Manage）
3. 要用 Android Studio 部署的話，終端機再執行：

```bash
~/Library/Android/sdk/platform-tools/adb connect 192.168.43.1:5555
```

看到 `connected to 192.168.43.1:5555` 就成功了。

**怎麼確認真的連到 Control Hub：** 電腦的 IP 應該是 `192.168.43.x`。如果是 `172.16.x.x`、`192.168.0.x` 之類的，表示連到的是別的路由器（例如附近有同名網路）。

```bash
ipconfig getifaddr en0
```

### 方法 B：USB-C 線（理想做法，目前還沒在隊上的 Mac 上成功過）

用 USB-C 線把電腦接到 **Control Hub 的 USB-C 孔**。好處是電腦的 Wi-Fi 可以一直連著有網路的那個，不用切來切去。

成功的話，Android Studio 裝置選單會出現 **REV Robotics Control Hub**。偵測不到時，照順序檢查：

1. **線能不能傳資料** —— 很多 USB-C 線只能充電。用同一條線接手機測試，手機沒反應就是線的問題
2. **插對孔** —— Control Hub 的 **USB-C** 孔，不是 USB-A 孔（那是給攝影機、遙控器用的）
3. **macOS 配件權限** —— 插上時 Mac 要是**解鎖**狀態，會跳出「允許配件連線嗎？」，要按**允許**。設定在 **系統設定 → 隱私權與安全性 → 配件**
4. **拿掉轉接頭 / USB hub**，直接插 Mac
5. **Control Hub 有亮綠燈**

確認電腦有沒有看到 Control Hub：

```bash
~/Library/Android/sdk/platform-tools/adb devices -l
```

清單是空的就是沒偵測到。也可以看 **系統報告 → USB** 有沒有出現新裝置。

### REV Hardware Client 2

REV 官方工具，**現在有 Mac 版**。用 USB 接上 Control Hub 後，可以看和改 Wi-Fi 名稱密碼、更新韌體。顯示 `No Hardware Detected` 就是 USB 沒接通，照上面的檢查清單排查。

---

## 5. 部署程式

兩種方式**選一種用，不要混用**。

### 方式 A：Android Studio（正式比賽程式用這個）

1. 確認電腦已經連上 Control Hub（第 4 節，`adb devices` 看得到）
2. 上方裝置選單選 **REV Robotics Control Hub**
   - 如果只看到 `Medium Phone API ...`，那是模擬器，**FTC 不能用**
   - **不要按 `Pair Devices Using Wi-Fi`**，那是給一般手機用的，跟 Control Hub 無關
3. 執行設定選 **TeamCode**，按綠色 ▶
4. 出現 `Launch succeeded` 後，Control Hub 上的 Robot Controller App 會自動重開

### 方式 B：OnBot Java（✅ 隊上已驗證可用）

不用 Android Studio，也不用 adb，只要瀏覽器。適合還沒搞定 USB / adb，或比賽現場臨時改數字。

1. 電腦連上 Control Hub 的 Wi-Fi，打開 http://192.168.43.1:8080
2. 點上方 **OnBotJava**，等頁面載入（左邊有 **Project Files** 檔案樹才是對的）
   - 注意：**Blocks 頁面的 `Upload OpMode` 只收 Blocks 檔**，不能拿來傳 `.java`
3. 上傳寫好的檔案：
   - 先點選左邊的 `org.firstinspires.ftc.teamcode` 資料夾
   - 點上方第 4 個圖示（**向上箭頭**）
   - 選檔視窗按 **⌘⇧G** 可以直接貼路徑
   - 或用 **＋** 新增檔案，把程式碼整個貼進去
4. 按右下角**紅色扳手** 🔧（Build Everything），等出現 **Build SUCCESSFUL**

### 兩者比較

| | OnBot Java | Android Studio |
|---|---|---|
| 準備 | 只要瀏覽器 | 要裝軟體、第一次部署較麻煩 |
| 編輯器 | 提示弱 | 自動完成、錯字即時標紅 |
| 程式存在 | **Control Hub 裡**（Hub 重置就沒了） | 電腦 + git，可備份回溯 |
| 外部套件（Road Runner、Pedro、FTC Dashboard） | 很難用 | 可以 |
| 網路 | 寫程式全程都要連 Control Hub | 寫、編譯時可以上網，只有部署要連 |

### ⚠️ 同名 class 衝突

OnBot Java 的檔案和 Android Studio 部署的程式**會同時存在 Control Hub 上**。如果兩邊都有 `Playground`，build 會失敗。

改回 Android Studio 部署前，**先到 OnBot Java 把同名檔案刪掉**。

---

## 6. 在 Driver Hub 上跑程式

1. 等 Driver Station 連上（部署後 Control Hub 會重開，約 10–20 秒）
2. 綁定遙控器：gamepad1 按住 **Start + A**，gamepad2 按住 **Start + B**
3. 從 **TeleOp** 或 **Autonomous** 下拉選單選程式
4. **INIT** → **▶ Start**，結束按 **■ Stop**

**第一次跑有馬達的新程式，先把機器人架高讓輪子懸空，手放在 Stop 上。**

### 沒有機器人也能測

`TeamCode` 裡的 **Playground** 只用 Control Hub 內建的東西，不用接馬達：

| 操作 | 效果 |
|---|---|
| 用手轉 Control Hub | 看 Yaw / Pitch / Roll 變化（IMU） |
| A / B / X | Control Hub LED 變綠 / 紅 / 藍 |
| Y | 遙控器震動（PS4 / PS5 手把才有） |
| Back | Yaw 歸零 |

---

## 7. 常見錯誤

| 症狀 | 原因 / 解法 |
|---|---|
| Android Studio 裝置選單只有 `Medium Phone API ...` | 電腦沒連上 Control Hub，回第 4 節 |
| 以為連上 Control Hub 的 Wi-Fi，但 `192.168.43.1:8080` 打不開 | 連到別的同名網路，檢查 IP 是不是 `192.168.43.x` |
| `adb devices` 清單是空的（USB） | 充電線、插錯孔、Mac 配件權限沒允許，見第 4 節方法 B |
| `adb connect` 卡住沒回應 | 電腦根本不在 Control Hub 的網路上 |
| REV Hardware Client 顯示 `No Hardware Detected` | USB 沒接通，同上 |
| Driver Hub 找不到 Control Hub 的網路 | Control Hub 還沒亮綠燈，或離太遠 |
| OnBot Java build 失敗，說 class 重複 | 跟 Android Studio 部署的程式同名，刪掉其中一個 |
| Driver Hub 選單找不到我的 OpMode | `@Disabled` 沒刪、檔案不在 `teamcode` 套件，或 build 沒成功 |
| 按 INIT 說找不到裝置 | 沒有 active 的 Configuration，或名稱跟程式不符（第 3 節） |
| 遙控器沒反應 | 沒按 Start + A 綁定 |
| Control Hub 一直重開機 | 電池電壓太低 |

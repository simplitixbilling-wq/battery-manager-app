"""
Simple MoviePy v2 demo — generates a 5-second MP4 with shapes and motion.
No ImageMagick required.
Run:  python video_demo.py
Requires:  pip install moviepy
"""
from moviepy import ColorClip, CompositeVideoClip

W, H = 1280, 720
FPS = 30
DURATION = 5

# 1. Background
bg = ColorClip(size=(W, H), color=(15, 20, 30), duration=DURATION)

# 2. Moving accent bar (blue)
bar = ColorClip(size=(W, 6), color=(74, 140, 255), duration=DURATION)
bar = bar.with_position(lambda t: ("center", H * 0.5 + 200 * (t / DURATION - 0.5)))

# 3. Pulsing circle (red) - using a small square as a "circle" approximation
circle = ColorClip(size=(60, 60), color=(255, 80, 80), duration=DURATION)
circle = circle.with_position(lambda t: (W * 0.2 + 300 * (t / DURATION), H * 0.3))

# 4. Sliding rectangle (green)
rect = ColorClip(size=(200, 120), color=(80, 220, 120), duration=DURATION)
rect = rect.with_position(lambda t: (W * 0.6 + 200 * (t / DURATION - 0.5), H * 0.7))

# 5. Compose & export
final = CompositeVideoClip([bg, bar, circle, rect], size=(W, H))
final.write_videofile(
    "output.mp4",
    fps=FPS,
    codec="libx264",
    audio_codec="aac",
    threads=4,
    preset="medium",
)

print("✅  output.mp4 created")
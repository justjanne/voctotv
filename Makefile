BACKGROUND_MAIN=\#FFFFFF
BACKGROUND_DEBUG=\#F8921E

.PHONY: all
all: \
	tv/src/main/res/mipmap-mdpi/ic_banner.png \
	tv/src/main/res/mipmap-hdpi/ic_banner.png \
	tv/src/main/res/mipmap-xhdpi/ic_banner.png \
	tv/src/main/res/mipmap-xxhdpi/ic_banner.png \
	tv/src/main/res/mipmap-xxxhdpi/ic_banner.png \
	tv/src/debug/res/mipmap-mdpi/ic_banner.png \
	tv/src/debug/res/mipmap-hdpi/ic_banner.png \
	tv/src/debug/res/mipmap-xhdpi/ic_banner.png \
	tv/src/debug/res/mipmap-xxhdpi/ic_banner.png \
	tv/src/debug/res/mipmap-xxxhdpi/ic_banner.png \
	tv/src/main/ic_banner-playstore.png

tv/src/main/res/mipmap-mdpi/ic_banner.png: design/icon_tv.svg
	inkscape --export-area-page --export-width=160 --export-background=$(BACKGROUND_MAIN) --export-filename=$@ $<

tv/src/main/res/mipmap-hdpi/ic_banner.png: design/icon_tv.svg
	inkscape --export-area-page --export-width=240 --export-background=$(BACKGROUND_MAIN) --export-filename=$@ $<

tv/src/main/res/mipmap-xhdpi/ic_banner.png: design/icon_tv.svg
	inkscape --export-area-page --export-width=320 --export-background=$(BACKGROUND_MAIN) --export-filename=$@ $<

tv/src/main/res/mipmap-xxhdpi/ic_banner.png: design/icon_tv.svg
	inkscape --export-area-page --export-width=480 --export-background=$(BACKGROUND_MAIN) --export-filename=$@ $<

tv/src/main/res/mipmap-xxxhdpi/ic_banner.png: design/icon_tv.svg
	inkscape --export-area-page --export-width=640 --export-background=$(BACKGROUND_MAIN) --export-filename=$@ $<

tv/src/debug/res/mipmap-mdpi/ic_banner.png: design/icon_tv.svg
	inkscape --export-area-page --export-width=160 --export-background=$(BACKGROUND_DEBUG) --export-filename=$@ $<

tv/src/debug/res/mipmap-hdpi/ic_banner.png: design/icon_tv.svg
	inkscape --export-area-page --export-width=240 --export-background=$(BACKGROUND_DEBUG) --export-filename=$@ $<

tv/src/debug/res/mipmap-xhdpi/ic_banner.png: design/icon_tv.svg
	inkscape --export-area-page --export-width=320 --export-background=$(BACKGROUND_DEBUG) --export-filename=$@ $<

tv/src/debug/res/mipmap-xxhdpi/ic_banner.png: design/icon_tv.svg
	inkscape --export-area-page --export-width=480 --export-background=$(BACKGROUND_DEBUG) --export-filename=$@ $<

tv/src/debug/res/mipmap-xxxhdpi/ic_banner.png: design/icon_tv.svg
	inkscape --export-area-page --export-width=640 --export-background=$(BACKGROUND_DEBUG) --export-filename=$@ $<

tv/src/main/ic_banner-playstore.png: design/icon_tv.svg
	inkscape --export-area-page --export-width=1280 --export-background=$(BACKGROUND_MAIN) --export-filename=$@ $<

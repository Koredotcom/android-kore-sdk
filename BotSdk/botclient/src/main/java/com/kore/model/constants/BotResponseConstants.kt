package com.kore.model.constants

object BotResponseConstants {
    const val COMPONENT_TYPE_TEMPLATE = "template"
    const val COMPONENT_TYPE_TEXT = "text"
    const val COMPONENT_TYPE_ERROR = "error"
    const val COMPONENT_TYPE_MESSAGE = "message"
    const val COMPONENT_TYPE_IMAGE = "image"
    const val COMPONENT_TYPE_IMAGE_URL = "image_url"
    const val COMPONENT_TYPE_LINK = "link"
    const val COMPONENT_TYPE_AUDIO = "audio"
    const val COMPONENT_TYPE_VIDEO = "video"

    const val TEMPLATE_TYPE_SYSTEM = "SYSTEM"
    const val TEMPLATE_TYPE_LIVE_AGENT = "live_agent"
    const val TEMPLATE_TYPE_BOT_ACTIVE = "Bot_Active"
    const val TEMPLATE_TYPE_BOT_KIT_UNREACHABLE = "botKitUnreachable"
    const val TEMPLATE_TYPE_BUTTON = "button"
    const val TEMPLATE_TYPE_LIST = "list"
    const val TEMPLATE_TYPE_PIE_CHART = "piechart"
    const val TEMPLATE_TYPE_LINE_CHART = "linechart"
    const val TEMPLATE_TYPE_BARCHART = "barchart"
    const val TEMPLATE_TYPE_TABLE = "table"
    const val TEMPLATE_TYPE_MINI_TABLE = "mini_table"
    const val TEMPLATE_TYPE_CAROUSEL = "carousel"
    const val TEMPLATE_TYPE_QUICK_REPLIES = "quick_replies"
    const val TEMPLATE_TYPE_MULTI_SELECT = "multi_select"
    const val TEMPLATE_TYPE_FORM = "form_template"
    const val TEMPLATE_TYPE_LIST_VIEW = "listView"
    const val TEMPLATE_TYPE_DATE = "dateTemplate"
    const val TEMPLATE_TYPE_DATE_RANGE = "daterange"
    const val TEMPLATE_TYPE_TABLE_LIST = "tableList"
    const val TEMPLATE_TYPE_FEEDBACK = "feedbackTemplate"
    const val TEMPLATE_TYPE_LIST_WIDGET = "List_widget"
    const val TEMPLATE_TYPE_LIST_WIDGET_2 = "listWidget"
    const val TEMPLATE_TYPE_DROPDOWN = "dropdown_template"
    const val TEMPLATE_TYPE_ADVANCED_LIST = "advancedListTemplate"
    const val TEMPLATE_TYPE_ADVANCED_MULTI_SELECT = "advanced_multi_select"
    const val TEMPLATE_TYPE_RADIO_OPTIONS = "radioOptionTemplate"
    const val TEMPLATE_TYPE_CLOCK = "clockTemplate"
    const val TEMPLATE_TYPE_CARD = "cardTemplate"
    const val TEMPLATE_TYPE_RESULTS = "search";

    //Theme Properties
    const val THEME_NAME = "KORE_PREFS"
    const val BUBBLE_LEFT_BG_COLOR = "BUBBLE_LEFT_BG_COLOR"
    const val BUBBLE_RIGHT_BG_COLOR = "BUBBLE_RIGHT_BG_COLOR"
    const val BUBBLE_LEFT_TEXT_COLOR = "BUBBLE_LEFT_TEXT_COLOR"
    const val BUBBLE_RIGHT_TEXT_COLOR = "BUBBLE_RIGHT_TEXT_COLOR"
    const val BUTTON_ACTIVE_BG_COLOR = "BUTTON_ACTIVE_BG_COLOR"
    const val BUTTON_ACTIVE_TXT_COLOR = "BUTTON_ACTIVE_TXT_COLOR"
    const val BUTTON_INACTIVE_BG_COLOR = "BUTTON_INACTIVE_BG_COLOR"
    const val BUTTON_INACTIVE_TXT_COLOR = "BUTTON_INACTIVE_TXT_COLOR"
    const val KEY_SUB_TITLE = "subtitle"
    const val PIE_TYPE = "pie_type"
    const val PIE_TYPE_DONUT = "donut"
    const val TIME_STAMP_TXT_COLOR = "TIME_STAMP_TXT_COLOR"
    const val TIME_STAMP_IS_BOTTOM = "TIME_STAMP_IS_BOTTOM"
    const val IS_TIME_STAMP_REQUIRED = "IS_TIME_STAMP_REQUIRED"
    const val BAR_CHART_DIRECTION_VERTICAL = "vertical"
    const val TABLE_VIEW_RESPONSIVE = "responsive"
    const val BUBBLE_STYLE = "BUBBLE_STYLE"
    const val VIEW_STAR = "star"
    const val VIEW_NPS = "NPS"
    const val VIEW_CSAT = "CSAT"
    const val VIEW_THUMBS_UP_DOWN = "ThumbsUpDown"
    const val SELECTED_FEEDBACK = "selectedFeedback"
    const val ROUNDED = "rounded"
    const val RECTANGLE = "rectangle"
    const val WIDGET_BG_COLOR = "WIDGET_BG_COLOR"
    const val WIDGET_TXT_COLOR = "WIDGET_TXT_COLOR"
    const val WIDGET_BORDER_COLOR = "WIDGET_BORDER_COLOR"
    const val WIDGET_DIVIDER_COLOR = "WIDGET_DIVIDER_COLOR"
    const val APPLY_THEME_NAME = "APPLY_THEME_NAME"
    const val MORE_COUNT = "moreCount"
    const val SEE_MORE = "seeMore"
    const val DEFAULT_ACTION = "default_action"
    const val DEFAULT = "default"
    const val POSTBACK = "postback"
    const val TYPE = "type"
    const val TEXT = "text"
    const val URL = "url"
    const val AUDIO_URL = "audioUrl"
    const val VIDEO_URL = "videoUrl"
    const val VALUE = "value"
    const val VALUES = "values"
    const val VALUES_CAP = "Values"
    const val DISPLAY_VALUES = "displayValues"
    const val PAYLOAD = "payload"
    const val WEB_URL = "web_url"
    const val USER_INTENT = "USERINTENT"
    const val KEY_TEMPLATE = "template"
    const val KEY_TEMPLATE_TYPE = "template_type"
    const val KEY_BUTTONS = "buttons"
    const val KEY_ELEMENTS = "elements"
    const val KEY_TEXT = "text"
    const val KEY_TITLE = "title"
    const val KEY_BOT_RESPONSE = "bot_response"
    const val KEY_EVENTS = "events"
    const val DIRECTION = "direction"
    const val X_AXIS = "X_axis"
    const val CARDS = "cards"
    const val CARD_HEADING = "cardHeading"
    const val CARD_DESCRIPTION = "cardDescription"
    const val DESCRIPTION = "description"
    const val ICON = "icon"
    const val DESCRIPTION_ICON = "descriptionIcon"
    const val ICON_SIZE = "iconSize"
    const val HEADER_STYLES = "headerStyles"
    const val COLOR = "color"
    const val BG_COLOR = "background-color"
    const val BORDER = "border"
    const val BORDER_LEFT = "border-left"
    const val BORDER_RIGHT = "border-right"
    const val BORDER_TOP = "border-top"
    const val BORDER_BOTTOM = "border-bottom"
    const val FONT_WEIGHT = "font_weight"
    const val BOLD = "bold"
    const val HEADER_EXTRA_INFO = "headerExtraInfo"
    const val DROP_DOWN = "dropdown"
    const val DROP_DOWN_OPTIONS = "dropdownOptions"
    const val FULL_WIDTH = "fullwidth"
    const val FIT_TO_WIDTH = "fitToWidth"
    const val FULL_WIDTH_ = "fullWidth"
    const val CARD_TYPE = "cardType"
    const val BUTTON_STYLES = "buttonStyles"
    const val BACKGROUND_COLOR = "background-color"
    const val BACKGROUND = "background"
    const val CARD_CONTENT_STYLES = "cardContentStyles"
    const val CARD_STYLES = "cardStyles"
    const val LIST_ITEMS = "listItems"
    const val DESCRIPTION_STYLES = "descriptionStyles"
    const val TITLE_STYLES = "titleStyles"
    const val ELEMENT_STYLES = "elementStyles"
    const val DESCRIPTION_ICON_ALIGNMENT = "descriptionIconAlignment"
    const val BORDER_WIDTH = "borderWidth"
    const val BORDER_RADIUS = "borderRadius"
    const val VIEW = "view"
    const val TEXT_INFORMATION = "textInformation"
    const val BUTTONS_LAYOUT = "buttonsLayout"
    const val BUTTONS_ALIGNMENT = "buttonAligment"
    const val DISPLAY_LIMIT = "displayLimit"
    const val COUNT = "count"
    const val OPTIONS = "options"
    const val OPTIONS_DATA = "optionsData"
    const val LABEL = "label"
    const val RIGHT = "right"
    const val TABLE = "table"
    const val TABLE_LIST_DATA = "tableListData"
    const val LARGE = "large"
    const val SMALL = "small"
    const val ROW_DATA = "rowData"
    const val RADIO = "radio"
    const val IS_CHECKED = "isChecked"
    const val HEADER_OPTIONS = "headerOptions"
    const val CONTENT_TYPE = "contenttype"
    const val BORDER_COLOR = "borderColor"
    const val STYLES = "styles"
    const val STYLE = "style"
    const val IS_COLLAPSED = "isCollapsed"
    const val VIDEO_CURRENT_POSITION = "videoCurrentPosition"
    const val DOMAIN = "domain"
    const val FIRST_NAME = "firstName"
    const val IS_VIDEO_CALL = "videoCall"
    const val SIP_USER = "sipUser"
    const val COLUMNS = "columns"
    const val CENTER = "center"
    const val LEFT = "left"
    const val TABLE_DESIGN = "table_design"
    const val PRIMARY = "primary"
    const val ADDITIONAL = "additional"
    const val FORMAT = "format"
    const val END_DATE = "endDate"
    const val START_DATE = "startDate"
    const val CONTENT_UNDERSCORE_TYPE = "content_type"
    const val NAME = "name"
    const val FILE_NAME = "fileName"
    const val MEDIA_TYPE = "mediaType"
    const val NOTE = "note"
    const val BACKGROUND_IMAGE = "bg_image"
    const val LOGO_URL = "logo_url"
    const val CUSTOM = "custom"
    const val ELEMENTS = "elements"
    const val SMILEY_ARRAYS = "smileyArrays"
    const val STAR_ARRAYS = "starArrays"
    const val NUMBERS_ARRAYS = "numbersArrays"
    const val NUMBER_ID = "numberId"
    const val COLLECTION_TITLE = "collectionTitle"
    const val COLLECTION = "collection"
    const val IMAGE_URL = "image_url"
    const val HEADING = "heading"
    const val TEXT_MESSAGE = "text_message"
    const val LIMIT = "limit"
    const val BUTTON_TYPE_WEB_URL = "web_url"
    const val BUTTON_TYPE_URL = "url"
    const val BUTTON_VARIATION = "variation"
    const val BUTTON_STACKED = "stackedButtons"
    const val CAROUSEL_STACKED = "stacked"
    const val PLAIN = "plain"
    const val CAROUSEL_TYPE = "carousel_type"
    const val TOP_SECTION = "topSection"
    const val MIDDLE_SECTION = "middleSection"
    const val BOTTOM_SECTION = "bottomSection"
    const val MENU = "menu"
    const val DETAILS = "details"
    const val ROW_ITEMS = "rowItems"
    const val SECTION_HEADER = "sectionHeader"
    const val SECTION_HEADER_DESC = "sectionHeaderDesc"
    const val IMAGE = "image"
    const val IMAGE_SRC = "image_src"
    const val RADIUS = "radius"
    const val LAYOUT = "layout"
    const val ROW_COLOR = "rowColor"
    const val KEY_BG_COLOR = "bgcolor"
    const val LINK = "link"
    const val MORE_DATA = "moreData"
    const val RADIO_OPTIONS = "radioOptions"
    const val SELECTED_POSITION = "selected_position"
    const val SELECTED_TIME = "selected_time"
    const val SELECTED_ITEM = "selected_item"
    const val PROGRESS = "progress"
    const val CHAT_BOT = "chatBot"
    const val FORM_FIELDS = "formFields"
    const val FORM_LABEL = "label"
    const val FORM_PLACE_HOLDER = "placeholder"
    const val FORM_FIELD_BUTTON = "fieldButton"
    const val DATA = "data"
    const val MESSAGE_TYPE_OUTGOING = "outgoing"
    const val LIST = "list"
    const val ICON_1 = "icon-1"
    const val ICON_2 = "icon-2"
    const val ICON_3 = "icon-3"
    const val ICON_4 = "icon-4"
    const val THUMBS_UP_DOWN_ARRAYS = "thumpsUpDownArrays"
    const val THUMBS_UP_ID = "thumpUpId"
    const val REVIEW_TEXT = "reviewText"
    const val STACKED = "stacked"
    const val GRAPH_ANSWER = "graph_answer"
    const val RESULTS = "results"
    const val WEB = "web"
    const val CENTER_PANEL = "center_panel"
    const val QUESTION = "question"
    const val ANSWER = "answer"
    const val PAGE_TITLE = "page_title"
    const val PAGE_PREVIEW = "page_preview"
    const val PAGE_IMAGE_URL = "page_image_url"
    const val PAGE_URL = "page_url"
    const val SYS_CONTENT_TYPE = "sys_content_type"
    const val SNIPPET_TITLE = "snippet_title"
    const val SNIPPET_CONTENT = "snippet_content"
    const val SOURCE = "source"
    const val FAQ = "faq"
    var DATE_FORMAT = "dd/MM/yyyy"
    var TIME_FORMAT = 24
}